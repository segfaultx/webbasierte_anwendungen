#!/usr/bin/env python3

import requests

URL = "http://localhost:8080/rest/sichtungen/1"
AUTHINFO = ("admin", "geheim")

r = requests.get(URL)

print(r.json())
