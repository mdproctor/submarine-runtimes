#some description
[when]There is a person with the name of {name} who likes {cheese}=Person(name=="{name}", likes=={cheese})
[when]There is some {cheese} cheese available=Cheese(type=="{cheese}")
[then]Add the message {message}=messages.add({message});
[when]string with eval=str : String() eval(str.equals("fire"))
