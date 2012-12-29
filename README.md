makeSheet
=========

an easy-to-use CLI utility to create LaTeX templates for university assignments

Ideal if you work on your assignments as team and use Dropbox to synchronize the assignments.

Usage
=========

Go to your terminal, enter the directory where you want to keep your assignments and type
  
  ms

When run for the first time in a directory makeSheet will create a course configuration.
The second time it is run it will prompt you for the details of the assignment.
You will need be prompted for the
* assignment number
* the due date (in a dd.MM format, eg "31.10")
* the names of the exercises (comma-separated list, eg "1,2,3" or "exercise 1, exercise 2")
The next time you run makeSheet in this directory, you will be provided with default values for each
of these.
* assignment number of the last assignment + 1
* the due date of the last assignment + 1 week
* the same exercises as last week


Following exemplary directory structure will be created

  .config.xml
  
  .lastUsage.xml
  
  1/
  
  1/assignment1.tex
  
  1/1.tex
  
  1/2.tex
  
assignment.tex will contain the LaTeX template and the lines

  \input{1}
  
  \input{2}

You can see the contents of the template at https://github.com/arsenij-solovjev/makeSheet/blob/master/src/main/resources/template.tex

This type of document layout is necessary if you want to edit an assignment which is under 
Dropbox synchronization in order to avoid conflicts and overwriting!


Requirements
=========
* Java 1.7
* Linux
* texlive 

Installation
=========

* Go to https://github.com/arsenij-solovjev/makeSheet/blob/master/bin/makesheet_2.9.2-0.9-one-jar.jar and click "View Raw"
* Save in a directory that is on your path
* Go to that directory and enter
  chmod a+x makesheet_2.9.2-0.9-one-jar.jar
* Add the following line to your .bashrc
  alias ms='makesheet_2.9.2-0.9-one-jar.jar'

TODOS
=========
* allow to override the default template for a directory 
* installation script (if you know how to do this, please share! because I don't)
