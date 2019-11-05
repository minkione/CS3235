import json

import socket
import ssl

from urllib.parse import urlparse
import re

from argparse import ArgumentParser

from texttable import Texttable


class MasterLockAPI:
  def __init__(self, key, username):
    self.username = username
    self.key = key

  def _log(self, str):
    print("[\33[32m+\33[0m] " + str)

  def _get_cred_param(self):
    return "apikey=" + self.key + "&username=" + self.username

  def _get_response(self, socket):
    response = ''
    while True:
      new = socket.recv(4096)
      if not new:
        socket.close()
        break
      try:
        response += new.decode('utf-8')
      except:
        continue
    return response

  def _make_query(self, scheme, url, data):
    url = urlparse(url)

    try:
      # Connect to server
      s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
      #s.settimeout(10)
      s.connect((url.hostname, 443))

      # SSL 
      s = ssl.wrap_socket(s, keyfile=None, certfile=None, server_side=False,
                          cert_reqs=ssl.CERT_NONE, ssl_version=ssl.PROTOCOL_SSLv23)
        
      # Setup request
      request = scheme + " " + url.path + '?' + url.query + " HTTP/1.1\r\n"
      request += "Host: " + url.hostname + "\r\n"
      request += "Connection: close\r\n"
      request += "Accept-Language: en\r\n"
      request += "User-Agent: com.masterlock.android\r\n"
      request += "AppVersion: 2.5.0.2\r\n"
      request += "Content-Type: application/json; charset=UTF-8\r\n"
      request += f"Content-Length: {len(data)}\r\n\r\n"
      request += data

      # Send request
      s.send(request.encode('ascii'))
    except:
      print("\33[31mCan't connect\33[0m")
      return ""

    return self._get_response(s)

  def create_guest(self, productId, firstName, lastName, organization):
    # Setup query
    api_url = f"https://api.masterlockvault.com/v5/product/{productId}/invitation"
    query = api_url + '?' + self._get_cred_param()

    self._log(f"Querying : {query}")

    # Json Guest data
    data = {
      "AlphaCountryCode": True, 
      "FirstName": firstName, 
      "Friday": True, 
      "LastName": lastName, 
      "PreferedPermissionsMode": "1", 
      "Monday": True, 
      "OpenShackle": False, 
      "Organization": organization, 
      "PhoneCountryCode": "1", 
      "Saturday": True,
      "ScheduleType": "1", 
      "Sunday": True, 
      "Thursday": True, 
      "Tuesday": True, 
      "ViewLocation": True, 
      "ViewTemporaryCode": True, 
      "Wednesday": True
    }

    self._log("Creating guest user")
    self._log(f"FirstName = {firstName}, LastName = {lastName}, Organization = {organization}")

    # for some reason this doesn't work ...
    #r = requests.post(api_url, data=json.dumps(data), headers=header, verify=True)
    #response = r.text
    
    # so I'm doing this the old fashion way
    # make query
    response = self._make_query("POST", query, json.dumps(data))
    response = re.search('{.*}', response).group(0)

    # print resulting invitation link
    self._log(f"New user invitation link : {json.loads(response)['InvitationURL']}")

  def get_guest(self, productId):
    # Setup query
    api_url = f"https://api.masterlockvault.com/v4/product/{productId}/invitation"
    query = api_url + '?' + self._get_cred_param()

    # make query
    response = self._make_query("GET", query, "")
    response = re.search('\[.*\]', response).group(0)
    response = json.loads(response)
    
    # print guest lists
    t = Texttable()
    t.header(["Name", "Guest ID", "GuestPermissions ID"])
    t.set_max_width(0)

    # print guest lists
    for i in response:
      t.add_row([i['Guest']['FirstName'], \
                 i['Guest']['Id'],        \
                 i['GuestPermissions']['Id']])

    print(t.draw())

  def get_locks(self):
    # Setup query
    api_url = f"https://api.masterlockvault.com/v4/product"
    query = api_url + '?' + self._get_cred_param()

    # make query 
    response = self._make_query("GET", query, "")
    response = re.search('\[.*\]', response).group(0)
    response = json.loads(response)
    
    # print locks info
    t = Texttable()
    t.header(["Lock Name", "Product ID"])
    t.set_max_width(0)
    for i in response:
      t.add_row([i['Name'], i['Id']])  

    print(t.draw())

  # timeOfday :
  #   1 = All Hours
  #   10 = Daytime
  #   11 = Nighttime
  def change_guest_permission(self, productId, guestPermissionId, days, timeOfday):
    # Setup query
    api_url = f"https://api.masterlockvault.com/v5/guestPermissions/{guestPermissionId}/update"
    query = api_url + '?' + self._get_cred_param()

    self._log(f"Querying : {query}")

    # Json guestPermissions data
    data = {
      "Friday": days[4], 
      "ViewMode": "2", 
      "Id": guestPermissionId, 
      "Monday": days[0], 
      "OpenShackle": False, 
      "Saturday": days[5], 
      "ScheduleType": timeOfday, 
      "Sunday": days[6], 
      "Thursday": days[3], 
      "Tuesday": days[1], 
      "ViewLocation": True, 
      "ViewTemporaryCode": True, 
      "Wednesday": days[2]
    }

    self._log("Changing guest permissions")
    self._log(f"Weekdays = {days}, timeOfDay = {timeOfday}")
    
    # make API query
    response = self._make_query("PUT", query, json.dumps(data))
    self._log("Done")

if __name__ == "__main__":
  # parse cmdline parameters
  parser = ArgumentParser(description="Script to communicate with MasterLock REST API")

  # create new guest param
  parser.add_argument("-n", "--firstname", dest="firstName", type=str, default="",
                      help="new user firstname")
  parser.add_argument("-N", "--lastname", dest="lastName", type=str, default=10,
                      help="new user lastname")
  parser.add_argument("-o", "--organization", dest="organization", type=str, default="",
                      help="new user organization")
  
  # change guest perm param
  parser.add_argument("-id", "--guestPermissionId", dest="guestPermissionId", type=str, default="",
                      help="guestPermissions Id to modify")
  parser.add_argument("-d", "--days", dest="days", nargs='*', type=str, default=[],
                      help="Weekdays permission")
  parser.add_argument("-t", "--time", dest="timeOfday", type=int, default=0,
                      help="Time of day permission (1 = All Hours, 10 = Daytime, 11 = Nighttime")

  # get guest list param
  parser.add_argument("-p", "--productId", dest="productId", type=str, default="",
                      help="target product id")


  args = parser.parse_args()

  # Creds
  key = "52F5590EAD633411F798C2B296E5DF8738FF824E654D046342F270CC5131F3D348A2986E7C1FE74414E4A36ADC7DC4014A3558673DF7581047A59F3CD21A28BBB544EFDE45D06609BDB65B73C336BE08DD7ACC1EF893BD698DF5922B7B21B51CCE063763F671495E4F8B066C9E62B7C6783BDD5F2D19C81152463600BBBA126023F59DD3FBD1D9D1B57F66DE2AEE7A69D4767CF5F4666880BA3E7BE6ED97458070AF1FAE04BE188767372DCDB792D03189FA9FFD50DF4F3A15C565DF8409927593230C05"
  username = "vinK283"

  api = MasterLockAPI(key, username)

  if (
    args.firstName     and \
    args.lastName      and \
    args.lastName      and \
    args.organization  and \
    args.productId
  ):
    # create a new guest
    api.create_guest(args.productId, args.firstName, args.lastName, args.organization)
  
  elif (
    args.guestPermissionId and \
    len(args.days) == 7    and \
    args.timeOfday != 0    and \
    args.productId
  ):
    # create allowed days bool array
    days = [x == "1" for x in args.days]

    # modify target guest permissions
    api.change_guest_permission(args.productId, args.guestPermissionId, days, args.timeOfday)
  
  elif args.productId:
    # get list of every guest for the given lock
    api.get_guest(args.productId)

  else:
    # get list of every locks linked to the given account
    api.get_locks()