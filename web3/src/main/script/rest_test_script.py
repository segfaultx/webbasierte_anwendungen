#!/usr/bin/env python3

import requests, sys

URL = f"http://localhost:8080/rest/sichtungen/"
URL = URL + sys.argv[1] if len(sys.argv) > 1 else URL
AUTHINFO = ("amatus", "hallo123")

r = requests.get(URL)

print(r.json())

new_comment = None
if type(r.json()) is list:
    for item in r.json():
        call = "http://localhost:8080" + item
        r = requests.get(call)
        new_comment = r.json()["commentList"][1]
        print(r.json())

new_comment["commentId"] = 0
new_comment["message"] = "yoyoyoyoo5"
#r = requests.put("http://localhost:8080/rest/sichtungen/1/kommentare/6", json=new_comment, auth=AUTHINFO)
# print(r.status_code)
# print(r.text)
r = requests.delete("http://localhost:8080/rest/sichtungen/1/kommentare/6", auth=AUTHINFO)

print(r.status_code)
print(r.text)
