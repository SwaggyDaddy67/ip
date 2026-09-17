# UI Test Plan

Each test case runs GLaDOS with a fixed sequence of typed commands (the
**Input**) and checks the full console output against the **Expected
output** block, character for character (line-ending differences between
Windows and Unix are ignored).

Update this file whenever a command's input format or reply wording
changes, so the test-ui skill keeps checking against current behaviour.

## Test 1: Add a todo, deadline, and event, then mark and unmark

**Aim:** Verify that all three task types can be added, that `list` shows
each one formatted with its type letter and status box, and that `mark`
and `unmark` both update the status shown afterwards.

**Input:**
```text
todo read book
deadline return book /by June 6th
event project meeting /from Aug 6th 2pm /to 4pm
mark 1
list
unmark 1
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: June 6th)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] read book
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
     2.[D][ ] return book (by: June 6th)
     3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
    ____________________________________________________________
    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [T][ ] read book
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[D][ ] return book (by: June 6th)
     3.[E][ ] project meeting (from: Aug 6th 2pm to: 4pm)
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

## Test 2: Reject a todo with no description

**Aim:** Verify that a bare `todo` command is rejected with an error message
instead of adding a blank task, and that a normal todo entered afterwards is
still added correctly.

**Input:**
```text
todo
todo read book
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     A todo with no description. Try again, with words this time.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

## Test 3: Reject an unrecognised command, then keep working normally

**Aim:** Verify that an unrecognised command and an empty todo are each
rejected with their own error message, and that a valid todo entered
afterwards is still added correctly, so a rejected command leaves the task
list untouched.

**Input:**
```text
blah
todo
todo read book
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     I have no idea what that was. Try one of: list, todo, deadline, event, mark, unmark, bye.
    ____________________________________________________________
    ____________________________________________________________
     A todo with no description. Try again, with words this time.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```

## Test 4: Reject invalid mark arguments, then mark successfully

**Aim:** Verify that `mark` with no number, a non-numeric argument, and an
out-of-range number are each rejected with their own message rather than
crashing, and that a valid `mark` still works afterwards.

**Input:**
```text
todo read book
mark
mark abc
mark 999
mark 1
list
bye
```

**Expected output:**
```text
    ____________________________________________________________
        ________          ____  ____  _____
       / ____/ /   ____ _/ __ \/ __ \/ ___/
      / / __/ /   / __ `/ / / / / / /\__ \ 
     / /_/ / /___/ /_/ / /_/ / /_/ /___/ / 
     \____/_____/\__,_/_____/\____//____/  

     Hello, I'm GLaDOS nice to... Oh, it's you.
     State your query. I have other tests to run.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Which task? Give me a number, like mark 2.
    ____________________________________________________________
    ____________________________________________________________
     "abc" is not a task number.
    ____________________________________________________________
    ____________________________________________________________
     There is no task 999. Your list has 1.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] read book
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][X] read book
    ____________________________________________________________
    ____________________________________________________________
     Test concluded. Try not to disappoint me next time.
    ____________________________________________________________
```
