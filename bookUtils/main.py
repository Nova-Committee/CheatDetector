inputStr = ""
while True:
    cur = input()
    if cur == "done": break
    inputStr += cur + "\n"

for line in inputStr.split("\n"):
    if line != "":
        print(line)
