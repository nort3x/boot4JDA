when request comes:
```
case 1: no trigger
       <-----|            <------|
             |                   |
request -> negate -> bundler -> auth -> execute
case 2: trigger
request -> negate -> trigger -> auth -> execute 
```
 