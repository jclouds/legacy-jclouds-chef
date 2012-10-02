jclouds Chef
============

[![Build Status](https://buildhive.cloudbees.com/job/jclouds/job/jclouds-chef/badge/icon)](https://buildhive.cloudbees.com/job/jclouds/job/jclouds-chef/)

This is the jclouds Chef api. It provides access to the different flavours of the Chef server api:

* [Chef community](http://www.opscode.com/chef/)
* [Hosted Chef](http://www.opscode.com/hosted-chef/)
* [Private Chef](http://www.opscode.com/private-chef/)

It currently supports versions **0.9** and **0.10** of the standard Chef server apis, and an initial
and very basic (still in progress) implementation of the user and organization api of the Hosted and
Private Chef flavours.

Also provides a set of utility methods to combine Chef features with the jclouds Compute service, allowing
users to customize the bootstrap process and manage the configuration of the deployed nodes.

Documentation
=============

You will find all documentation in www.jclouds.org

