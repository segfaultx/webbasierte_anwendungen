#!/usr/bin/env python3

import sys, requests, re, random

ptrn = re.compile(sys.argv[1])

trollusers = [("troll1", "troll"), ("troll2", "troll"), ("troll3", "troll")]

comments_to_troll = []  # remember comment ids to troll for later
# get all sightings + comments

r = requests.get("http://localhost:8080/rest/sichtungen")

for item in r.json():
    req = requests.get("http://localhost:8080" + item)
    for comment in req.json()["commentList"]:
        if ptrn.match(comment["fullname"]):
            comments_to_troll.append(item.split("/")[-1])
for sichtung in comments_to_troll:
    trolluser = random.choice(trollusers)
    cmt = {'commentId': 0, 'message': "FAKE NEWS!!!!",
           "fullname": trolluser[0], 'creationDate': '2019-06-18'}
    r = requests.post(f"http://localhost:8080/rest/sichtungen/{sichtung}/kommentare",
                      json=cmt,
                      auth=trolluser)
    print(r.status_code)
    print(r.text)
