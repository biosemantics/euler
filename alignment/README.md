euler-alignment
============
A gwt-based UI for the for the [EulerX toolkit](https://github.com/EulerProject/EulerX)


Contribution
----------

### Setup
If you want to contribute, the source is built using Maven and Google Web Toolkit.
In Eclipse you can therefore use:
* m2e - Maven Integration for Eclipse (e.g. for Juno version: http://download.eclipse.org/releases/juno)
* Google Plugin for Eclipse (https://developers.google.com/eclipse/)

and 

1. configure your Eclipse project to be a Maven Project and to use Google Web Toolkit (2.6)
2. run `mvn package` to set up `/src/main/webapp/` files for GWT dev mode. Run again for changes in the directory

Please [configure your git](http://git-scm.com/book/en/Customizing-Git-Git-Configuration) for this repository as:
* `core.autocrlf` true if you are on Windows 
* or `core.autocrlf input` if you are on a Unix-type OS

### Run Dev Mode

#### Class
`com.google.gwt.dev.DevMode`

#### Arguments
`-remoteUI "${gwt_remote_ui_server_port}:${unique_id}" -startupUrl Euler_alignment.html -logLevel INFO -port auto -codeServerPort auto -war **full_path_to_your_git_dir**\euler-alignment\target\euler-alignment-0.0.1-SNAPSHOT edu.arizona.biosemantics.euler.alignment.Euler_alignment`
