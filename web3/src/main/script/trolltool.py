#!/usr/bin/env python3

import sys, requests

trollusers = [("troll1", "troll"), ("troll2", "troll"), ("troll3", "troll")]

comments_to_troll = [] # remember comment ids to troll for later
# get all sightings + comments

r = requests.get("http://localhost:8080/rest/sichtungen")

for item in r.json():
    req = requests.get("http://localhost:8080/"+item)
    for comment in req.json()["commentList"]:
        print(comment["fullname"])
