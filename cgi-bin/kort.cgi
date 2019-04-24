#!/usr/bin/env python3
import cgi, cgitb, uuid, random, shelve, os
from http.cookies import SimpleCookie
DBPATH = os.path.expanduser("~/Studium_Medieninformatik_Semester6/Webbasierte-Anwendungen/sessiondata2")
mysessionID = "mysessionID"
params = cgi.FieldStorage()
cookie = SimpleCookie(os.environ.get("HTTP_COOKIE",""))
if mysessionID in cookie:
    sessionid = cookie[mysessionID].value
else:
    sessionid = str(uuid.uuid4())
    cookie[mysessionID] = sessionid
db = shelve.open(DBPATH, writeback=True)

def show_ask_name():
    print("Content-type: text/html")
    print(cookie.output())
    print()
    print('''<p> Bitte Namen eingeben: </p>
                <form action={contextroot} method="POST"/>
                Name: <input type="text" name="usrname"/>
                <input type="submit"/>
                    </form>'''.format(contextroot=os.environ["SCRIPT_NAME"]))

def save_user():
    if sessionid not in db.keys():
        db[sessionid] = {}
        db[sessionid]["user"] = params["usrname"].value
        db[sessionid]["correct"] = 0

def show_riddle():
    print()

print()


if __name__ == "__main__":
    cgitb.enable(os.path.expanduser("~/Studium_Medieninformatik_Semester6/Webbasierte-Anwendungen/cgi-bin/logs"))
    if os.environ["REQUEST_METHOD"] == "GET":
        show_ask_name()
    if os.environ["REQUEST_METHOD"] == "POST":
        save_user()
        show_riddle()