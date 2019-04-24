#!/usr/bin/env python3
import cgi, cgitb, uuid, random, shelve, os, time
from http.cookies import SimpleCookie
DBPATH = os.path.expanduser("~/Studium_Medieninformatik_Semester6/Webbasierte-Anwendungen/sessiondata2")
mysessionID = "mysessionID"
params = cgi.FieldStorage()
cookie = SimpleCookie(os.environ.get("HTTP_COOKIE",""))
timeout = 15
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
        db[sessionid]["riddletime"] = []

def show_riddle():
    if not db[sessionid]["riddletime"]:
        db[sessionid]["riddletime"].append(time.time())
    if (time.time() - db[sessionid]["riddletime"][-1]) < timeout:
        db[sessionid]["riddletime"].append(time.time())
    print("Content-type: text/html")
    print(cookie.output())
    print()
    print('''<p>{name}, was ist {num1} + {num2} ?</p>
    <form action={chroot}/check method="POST">
    Antwort: <input type="text"/>
    <input type="submit"/>'''.format(chroot=os.environ["SCRIPT_NAME"],name=db[sessionid]["user"],num1=random.randint(1,18),num2=random.randint(1,18)))

def check_input():
    if (time.time() - db[sessionid]["riddletime"][-1]) <= timeout:
        print("Content-type: text/plain")
        print()
        print("WITHIN TIME")
    else:
        print("Content-type: text/plain")
        print()
        print("Too late!")

if __name__ == "__main__":
    cgitb.enable(os.path.expanduser("~/Studium_Medieninformatik_Semester6/Webbasierte-Anwendungen/cgi-bin/logs"))
    if os.environ["REQUEST_METHOD"] == "GET":
        show_ask_name()
    if os.environ["REQUEST_METHOD"] == "POST":
        if os.environ["PATH_INFO"] == "/check":
            check_input()
        else:
            save_user()
            show_riddle()