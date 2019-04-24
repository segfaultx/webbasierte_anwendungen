#!/usr/bin/env python3
import cgi, cgitb, uuid, random, shelve, os, time
from http.cookies import SimpleCookie
DBPATH = os.path.expanduser("~/Studium_Medieninformatik_Semester6/Webbasierte-Anwendungen/sessiondata2")
mysessionID = "mysessionID"
params = cgi.FieldStorage()
cookie = SimpleCookie(os.environ.get("HTTP_COOKIE",""))
timeout = 15
current_riddle = "{}+{}".format(random.randint(1,18), random.randint(1,18))
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
    db[sessionid]["riddletime"].append(time.time())
    db[sessionid]["riddle"] = current_riddle
    print("Content-type: text/html")
    print(cookie.output())
    print()
    print('''<p>{name}, was ist {riddle} ?</p>
    <form action={chroot}/check method="POST">
    Antwort: <input type="text" name="answer"/>
    <input type="submit"/>'''.format(chroot=os.environ["SCRIPT_NAME"],name=db[sessionid]["user"],riddle=current_riddle))

def show_win_screen():
    db[sessionid]["correct"] = 0
    answer = '''<p>Glueckwunsch {}! Du hast {} Sekunden gebraucht
     um 3 Aufgaben in Folge zu loesen!</p>
     <form action={}/show_riddle method="POST">
     <input type="submit" value="Nochmal"/>
     </form>'''.format(db[sessionid]["user"], int(sum([time.time()-entry for entry in db[sessionid]["riddletime"]])), os.environ["SCRIPT_NAME"])
    db[sessionid]["riddletime"] = []
    print("Content-type: text/html")
    print(cookie.output())
    print()
    print(answer)

def check_input():
    if (time.time() - db[sessionid]["riddletime"][-1]) <= timeout:
        if eval(params["answer"].value) == eval(db[sessionid]["riddle"]):      
            db[sessionid]["correct"]+=1
            if db[sessionid]["correct"] == 3:
                show_win_screen()
            else: 
                show_riddle()
        else:
            db[sessionid]["correct"] = 0
            print("Content-type: text/html")
            print(cookie.output())
            print()
            print('''<p>Sorry {}, das war leider nicht korrekt. Die richtige Antwort waere {} gewesen.</p>
                    <form action={}/show_riddle method="POST">
                    <input type="submit" value="Nochmal Versuchen"/>
                    </form>'''.format(db[sessionid]["user"],eval(db[sessionid]["riddle"]),os.environ["SCRIPT_NAME"]))
    else:
        db[sessionid]["correct"] = 0
        print("Content-type: text/html")
        print(cookie.output())
        print()
        print('''<p>Sorry {}, das war leider zu langsam, versuch es doch nochmal!</p>
                    <form action={}/show_riddle method="POST">
                    <input type="submit" value="Nochmal Versuchen"/>
                    </form>'''.format(db[sessionid]["user"],os.environ["SCRIPT_NAME"]))

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