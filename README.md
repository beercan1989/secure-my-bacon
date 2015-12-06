Secure My Bacon
===============
[![Build Status](https://travis-ci.org/beercan1989/secure-my-bacon.svg)](https://travis-ci.org/beercan1989/secure-my-bacon)
[![Codacy Badge](https://api.codacy.com/project/badge/grade/a03b8713f2694a8f9a4af79935721760)](https://www.codacy.com/app/beercan1989/secure-my-bacon)
[![Coverage Status](https://coveralls.io/repos/beercan1989/secure-my-bacon/badge.svg?branch=master&service=github)](https://coveralls.io/github/beercan1989/secure-my-bacon?branch=master)
[![Maven Central](https://img.shields.io/maven-central/v/uk.co.baconi.secure/platform.svg?label=secure-my-bacon)](https://maven-badges.herokuapp.com/maven-central/uk.co.baconi.secure/platform)  
Its like 'saving ones bacon' only its more secure.

What are you on about?
----------------------
I've had idea's about how I could provide a system to save things securley, basically a password system but its not just for single passwords. Its nothing new, it has already been done before and probably many times over.

What are you going to use?
--------------------------
0. Java 8
1. Tomcat 8
2. Spring Boot
3. Graph Database (Neo4J / Orient DB)
4. Nice Configuration (Spring Properties / Typesafe Config)
5. Substeps

Whats the road map like?
------------------------
* **0.0.1** - Travis-CI, Codacy, Coveralls, Reviewable integration
* **0.1.0** - Basic api and encryption
* **0.1.5** - Substeps coverage for the basic api
* **0.2.0** - Basic frontend to consume the existing api
* **0.2.5** - Substeps coverage for the basic frontend
* **0.3.0** - Registration and sharing
* **0.4.0** - Password recovery process via administrator accounts
* **0.5.0** - Run standalone without Tomcat, aka desktop mode
