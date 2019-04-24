#!/usr/bin/env python3
import os, cgi, cgitb, shelve, uuid, time

params = cgi.FieldStorage()
mysessionKEY = "mysessionID"
DBPATH = os.path.expanduser("~/Studium_Medieninformatik_Semester6/Webbasierte-Anwendungen/sessiondata")
sessionid = params.getvalue(mysessionKEY, str(uuid.uuid4()))
db = shelve.open(DBPATH, writeback=True)
timeout = 30
def show_upload(sid):
    username = db[sid].get("name",0)
    if username != 0:
        print("Content-Type: text/html")
        print()
        print('''<h1>{usrname}</h1>
                 <form action={contextroot}/upload method=POST>
                       Upload:  <input type="file" name="Data"/>
                       <input type="submit"/>
                       <input type="hidden" name="{skey}" value="{sval}"/>
                   </form>'''.format(usrname=username,contextroot=os.environ["SCRIPT_NAME"], skey=mysessionKEY,sval=sessionid))
    else:
        show_login()
                


def show_login():
    print("Content-Type: text/html")
    print()
    print('''<form action={contextroot}/login method=POST>
                Username:  <input type="text" name="Username"/>
                Password: <input type="password" name="Password" required/>
                <input type="submit"/>
                <input type="hidden" name="{skey}" value="{sval}"/>
            </form>'''.format(contextroot=os.environ["SCRIPT_NAME"], skey=mysessionKEY,sval=sessionid))

def check_login():
    
    if params["Password"].value == params["Username"].value[::-1]:
        if sessionid not in db.keys():
            db[sessionid] = {}
            db[sessionid]["name"] = params["Username"].value
            db[sessionid]["logintime"] = time.time()
        else:
            db[sessionid]["logintime"] = time.time()
        show_upload(sessionid)
    else:
        show_login()

if __name__ == "__main__":
    cgitb.enable(os.path.expanduser("~/Studium_Medieninformatik_Semester6/Webbasierte-Anwendungen/cgi-bin/logs"))
    if os.environ["REQUEST_METHOD"] == "GET":
        show_login()
    if os.environ["REQUEST_METHOD"] == "POST" and os.environ["PATH_INFO"] == "/login":
       check_login()
    if os.environ["REQUEST_METHOD"] == "POST" and os.environ["PATH_INFO"] == "/upload":
        sessionid = params.getvalue(mysessionKEY, 0)
        if sessionid in db.keys():
            if (time.time()-db[sessionid]["logintime"]<timeout):
                print("Content-type: text/plain")
                print()
                print("Ok")
            else:
                show_login()
        else:
            show_login()