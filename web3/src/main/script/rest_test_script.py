#!/usr/bin/env python3

import requests, sys

URL = f"http://localhost:8080/rest/sichtungen/"
URL = URL+sys.argv[1] if len(sys.argv)>1 else URL
AUTHINFO = ("admin", "geheim")

r = requests.get(URL)

print(r.json())

if type(r.json()) is list:
    for item in r.json():
        call = "http://localhost:8080" + item
        r = requests.get(call)
        print(r.json())
