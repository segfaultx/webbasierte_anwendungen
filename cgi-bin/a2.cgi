#!/usr/bin/env python3
import os, cgi, cgitb

def show_login():
    print("Content-Type: text/html")
    print()
    print('''<form action={}/login method=POST>
                Username:  <input type="text" name="Username"/>
                Password: <input type="password" name="Password" required/>
                <input type="submit"/>
            </form>'''.format(os.environ["SCRIPT_NAME"]))

def check_login():
    params = cgi.FieldStorage()
    if params["Password"].value == params["Username"].value[::-1]:
        print("Content-Type: text/html")
        print()
        print("OK")
    else:
        show_login()

if __name__ == "__main__":
    cgitb.enable(os.path.expanduser("~/Studium_Medieninformatik_Semester6/Webbasierte-Anwendungen/cgi-bin/logs"))
    if os.environ["REQUEST_METHOD"] == "GET":
        show_login()
    if os.environ["REQUEST_METHOD"] == "POST" and os.environ["PATH_INFO"] == "/login":
       check_login()