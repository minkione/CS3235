# Python Script to Query the Lock REST API

for help 
```sh
python3 masterlock_api.py -h
```


## Examples
### Get the current guests and their IDs
```sh
python3 masterlock_api.py
```
### Add a new guest with unlimited permissions
```sh
python3 masterlock_api.py -n sah -N quel -o plaisir
```

### Change some guest permissions:
```sh
python3 masterlock_api.py -d 1 0 0 0 0 1 0 -t 10 -id c1bd8b8d-f081-4aef-aaf8-97fa5ac92bcc
```
