# Zainali
Zai nali (Chinese) means "where is it?" or "There it is! "Just by changing the tone, you can change the meaning. This app will not have voice recognition...but by changing the location it should give you different results. :)

## Introduction
When opening the app you will see the map. It will try and find your location and point it on the map. Or you could open the navigation drawer and choose one of the locations.

## The how
At the start of the app it will do two things in the background
- getting the list of countries
- trying to find your location and find it in the list

When it finds your country, 3 things will happen:
- zoom in on the country
- adds a marker on the map with the name of that country
- puts borders of the country on the map

## The process
This was my first time, working with Google maps and for reading out a file. A lot of time went into understanding the mechanics behind it. For example, understanding the KML, reading out only the things that you need and then convert them into objects that you need. I had a lot of trouble with:
- finding an algorithm that finds a point in the polygon
- slowness, it just takes a lot of time to find and check the point.

## What I would like to fix
I guess I'm not fixing anything anymore, but these are the things that I would really like to fix now:
- Make it faster, I guess I need to slack down on object creation and use something persistant for my created objects.
- styling, maybe I would spice it up a bit more with Material style. Like using the Floating Action Button, make a cardview. But also using more of the styles.xml and re-use styles for my views.
- Clear up the code, seperating the logic from the layout. It's still a bit messy for my taste. 




