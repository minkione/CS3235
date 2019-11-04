import requests
import json
import socket
import ssl
from urllib.parse import urlparse
import re

from argparse import ArgumentParser

def get_response(socket):
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


def query_api(scheme, url, data):
    url = urlparse(url)

    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        #s.settimeout(10)
        s.connect((url.hostname, 443))

        s = ssl.wrap_socket(s, keyfile=None, certfile=None, server_side=False,
                          cert_reqs=ssl.CERT_NONE, ssl_version=ssl.PROTOCOL_SSLv23)
      
        request = scheme + " " + url.path + '?' + url.query + " HTTP/1.1\r\n"
        request += "Host: " + url.hostname + "\r\n"
        request += "Connection: close\r\n"
        request += "Accept-Language: en\r\n"
        request += "User-Agent: com.masterlock.android\r\n"
        request += "AppVersion: 2.5.0.2\r\n"
        request += "Content-Type: application/json; charset=UTF-8\r\n"
        request += f"Content-Length: {len(data)}\r\n\r\n"
        request += data
        s.send(request.encode('ascii'))
    except:
        print("\33[31mCan't connect            \33[0m\t{:25s}".format("https://" + hostname))
        return ""


    response = get_response(s)
    #return re.search('{.*}', response).group(0)
    return response


def create_guest(key, username, productId, firstName, lastName, organization):
    api_url = f"https://api.masterlockvault.com/v5/product/{productId}/invitation"
    query = api_url + '?apikey=' + key + "&username=" + username

    header = {'Connection':'close','Accept-Language':'en','Content-Type':'application/json; charset=UTF-8','User-Agent':'com.masterlock.android','AppVersion':'2.5.0.2'}

    print(f"[+] quering : {query}")

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

    print("[+] Creating guest user")
    print(f"[+] FirstName = {firstName}, LastName = {lastName}, Organization = {organization}")

    # for some reason this doesn't work ...
    #r = requests.post(api_url, data=json.dumps(data), headers=header, verify=True)
    #response = r.text
    
    # so I'm doing this the old fashion way
    response = query_api("POST", query, json.dumps(data))
    response = re.search('{.*}', response).group(0)

    print(f"[+] new user invitation link : {json.loads(response)['InvitationURL']}" )

# https://api.masterlockvault.com/v4/product/5b439e4e-b0fd-4f42-b8dd-4016b43e0706/invitation?apikey=52F5590EAD633411F798C2B296E5DF8738FF824E654D046342F270CC5131F3D348A2986E7C1FE74414E4A36ADC7DC4014A3558673DF7581047A59F3CD21A28BBB544EFDE45D06609BDB65B73C336BE08DD7ACC1EF893BD698DF5922B7B21B51CCE063763F671495E4F8B066C9E62B7C6783BDD5F2D19C81152463600BBBA126023F59DD3FBD1D9D1B57F66DE2AEE7A69D4767CF5F4666880BA3E7BE6ED97458070AF1FAE04BE188767372DCDB792D03189FA9FFD50DF4F3A15C565DF8409927593230C05&username=vinK283
def get_guest(key, username, productId):
    api_url = f"https://api.masterlockvault.com/v4/product/{productId}/invitation"
    query = api_url + '?apikey=' + key + "&username=" + username

    response = query_api("GET", query, "")
    response = re.search('\[.*\]', response).group(0)
    response = json.loads(response)
    print("\33[2m  Name                  Guest Id                           GuestPermissions Id        \33[0m")
    print("======================================================================================")
    for i in response:
        print(f"{i['Guest']['FirstName']}\t - {i['Guest']['Id']} - {i['GuestPermissions']['Id']}")


# timeOfday :
#   1 = All Hours
#   10 = Daytime
#   11 = Nighttime
def change_guest_permission(key, username, productId, guestPermissionId, days, timeOfday):
        api_url = f"https://api.masterlockvault.com/v5/guestPermissions/{guestPermissionId}/update"
        query = api_url + '?apikey=' + key + "&username=" + username
        header = {'Connection':'close','Accept-Language':'en','Content-Type':'application/json; charset=UTF-8','User-Agent':'com.masterlock.android','AppVersion':'2.5.0.2'}

        print(f"[+] quering : {query}")

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

        print("[+] Changing guest permission")
        print(f"[+] Weekdays = {days}, timeOfDay = {timeOfday}")
        
        response = query_api("PUT", query, json.dumps(data))
        print("[+] Done")

key = "52F5590EAD633411F798C2B296E5DF8738FF824E654D046342F270CC5131F3D348A2986E7C1FE74414E4A36ADC7DC4014A3558673DF7581047A59F3CD21A28BBB544EFDE45D06609BDB65B73C336BE08DD7ACC1EF893BD698DF5922B7B21B51CCE063763F671495E4F8B066C9E62B7C6783BDD5F2D19C81152463600BBBA126023F59DD3FBD1D9D1B57F66DE2AEE7A69D4767CF5F4666880BA3E7BE6ED97458070AF1FAE04BE188767372DCDB792D03189FA9FFD50DF4F3A15C565DF8409927593230C05"
username = "vinK283"
#firstName = "sah"
#lastName = "quel"
#organization = "plaisir"
productId = "5b439e4e-b0fd-4f42-b8dd-4016b43e0706"
#guestPermissionId = "223f4c03-c810-4445-87a1-cf3a27558c1f"



if __name__ == "__main__":
  parser = ArgumentParser(description="Script to communicate with MasterLock REST API")

  parser.add_argument("-n", "--firstname", dest="firstName", type=str, default="",
                      help="new user firstname")
  parser.add_argument("-N", "--lastname", dest="lastName", type=str, default=10,
                      help="new user lastname")
  parser.add_argument("-o", "--organization", dest="organization", type=str, default="",
                      help="new user organization")
  
  parser.add_argument("-id", "--guestPermissionId", dest="guestPermissionId", type=str, default="",
                      help="guestPermissions Id to modify")
  parser.add_argument("-d", "--days", dest="days", nargs='*', type=str, default=[],
                      help="Weekdays permission")
  parser.add_argument("-t", "--time", dest="timeOfday", type=int, default=0,
                      help="Time of day permission (1 = All Hours, 10 = Daytime, 11 = Nighttime")

  args = parser.parse_args()

  if args.firstName and args.lastName and args.organization:
    create_guest(key, username, productId, args.firstName, args.lastName, args.organization)
  elif args.guestPermissionId and len(args.days) == 7 and args.timeOfday != 0:
    days = []
    for i in args.days:
        if i != "0":
            days.append(True)
        else:
            days.append(False) 
    change_guest_permission(key, username, productId, args.guestPermissionId, days, args.timeOfday)
  else:
    get_guest(key, username, productId)

"""
create_guest(key, username, productId, firstName, lastName, organization)
get_guest(key, username, productId)
change_guest_permission(key, username, productId, guestPermissionId, [True, False, True, False, True, False, False], 11)
"""