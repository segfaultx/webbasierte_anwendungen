#!/usr/bin/env python3
import cgi, cgitb, os

def dump():
    variables = ""
    for key in sorted(os.environ):
        variables += "<li>{}: {}</li>".format(key,os.environ[key])
    result = variables
    variables = ""
    for k,v in cgi.FieldStorage():
        variables += "<li>{}: {}</li>".format(k,v)
    result +=variables
    result = "<ul>{}</ul>".format(result)
    return result

if __name__ == "__main__":
    cgitb.enable(logdir=os.path.expanduser("~/logs"))
    result = dump()
    print("Content-Type: text/html")
    print()
    print(result)