# Python Script to Query the MasterLock REST API

for help 
```sh
python3 masterlock_api.py -h
```

## Examples

### Get list of locks linked to the account
```
python3 masterlock_api.py
```
```
+------------------------------+--------------------------------------+
|          Lock Name           |              Product ID              |
+==============================+======================================+
| Bluetooth Smart 4400 Padlock | 5b439e4e-b0fd-4f42-b8dd-4016b43e0706 |
+------------------------------+--------------------------------------+
```

### Get list of guest user 
```
python masterlock_api.py -p 5b439e4e-b0fd-4f42-b8dd-4016b43e0706
```
```
+--------+--------------------------------------+--------------------------------------+
|  Name  |               Guest ID               |         GuestPermissions ID          |
+========+======================================+======================================+
| Travis | 9fc3ea25-2f83-4d73-9880-239b0c152a0b | d56bc3f9-a38e-49cb-912a-ba058997efc2 |
+--------+--------------------------------------+--------------------------------------+
| Deric  | b413891d-58d2-4242-8989-2ceb43485639 | ae34cecd-efc7-4f5a-a5b4-f7bc63b001af |
+--------+--------------------------------------+--------------------------------------+
| sah3   | a419ee08-76fb-4dfe-b74d-583d628629ad | a3bea51b-9aad-4d9c-9e85-d5d35d7e0acd |
+--------+--------------------------------------+--------------------------------------+
| sah2   | 27c84d9f-9fc3-484a-8ed1-936300a30bd2 | a35935ce-e4b8-4358-990a-2ab3c701cb4d |
+--------+--------------------------------------+--------------------------------------+
| Max    | 33941dde-0b9c-47b9-8358-e2e94ea293c0 | 223f4c03-c810-4445-87a1-cf3a27558c1f |
+--------+--------------------------------------+--------------------------------------+
| sah    | 9779ef45-7650-4803-b56a-f9fdd4f9c0d1 | c1bd8b8d-f081-4aef-aaf8-97fa5ac92bcc |
+--------+--------------------------------------+--------------------------------------+
```

### Create a new guest user
```
python masterlock_api.py -n FirstName -N LastName -o Organization -p 5b439e4e-b0fd-4f42-b8dd-4016b43e0706
```
```
[+] Querying : https://api.masterlockvault.com/v5/product/5b439e4e-b0fd-4f42-b8dd-4016b43e0706/invitation?apikey=52F5590EAD633411F798C2B296E5DF8738FF824E654D046342F270CC5131F3D348A2986E7C1FE74414E4A36ADC7DC4014A3558673DF7581047A59F3CD21A28BBB544EFDE45D06609BDB65B73C336BE08DD7ACC1EF893BD698DF5922B7B21B51CCE063763F671495E4F8B066C9E62B7C6783BDD5F2D19C81152463600BBBA126023F59DD3FBD1D9D1B57F66DE2AEE7A69D4767CF5F4666880BA3E7BE6ED97458070AF1FAE04BE188767372DCDB792D03189FA9FFD50DF4F3A15C565DF8409927593230C05&username=vinK283
[+] Creating guest user
[+] FirstName = FirstName, LastName = LastName, Organization = Organization
[+] New user invitation link : https://www.masterlockvault.com/invitation/product/91041f08-0acd-4da4-8c2c-49a2e9c0aa6d

```

### Change some guest permissions:
```
python masterlock_api.py -d 1 1 1 1 1 1 1 -t 1 -id c1bd8b8d-f081-4aef-aaf8-97fa5ac92bcc -p 5b439e4e-b0fd-4f42-b8dd-4016b43e0706
```
```
[+] Querying : https://api.masterlockvault.com/v5/guestPermissions/c1bd8b8d-f081-4aef-aaf8-97fa5ac92bcc/update?apikey=52F5590EAD633411F798C2B296E5DF8738FF824E654D046342F270CC5131F3D348A2986E7C1FE74414E4A36ADC7DC4014A3558673DF7581047A59F3CD21A28BBB544EFDE45D06609BDB65B73C336BE08DD7ACC1EF893BD698DF5922B7B21B51CCE063763F671495E4F8B066C9E62B7C6783BDD5F2D19C81152463600BBBA126023F59DD3FBD1D9D1B57F66DE2AEE7A69D4767CF5F4666880BA3E7BE6ED97458070AF1FAE04BE188767372DCDB792D03189FA9FFD50DF4F3A15C565DF8409927593230C05&username=vinK283
[+] Changing guest permissions
[+] Weekdays = [True, True, True, True, True, True, True], timeOfDay = 1
[+] Done
```
