from pyperclip import copy, paste

inputStr = ""
while True:
    cur = input()
    if cur == "done": break
    inputStr += cur + "\n"

result = ""
for s in (i for i in inputStr.split("\n") if i != ""):
    result += s + "\n"

copy(result)
