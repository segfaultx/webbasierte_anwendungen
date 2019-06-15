#!/usr/bin/env python3

import requests, sys

arg = sys.argv[1]

URL = f"http://localhost:8080/rest/sichtungen/{arg}"
AUTHINFO = ("admin", "geheim")

r = requests.get(URL)

print(r.json())
