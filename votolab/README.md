# votolab

This simple project, is a simulation of an application that allows for voting.
It provides an endpoint to send votes to some sort of subject in order to be asynchronously processed.
Each vote is sent to a standard SQS Queue backed by a simple consumer written from scratch using only AWS SDK and Executor framework from Java concurrency API.

## Tecnhologies

* AWS SQS
* Framework executor of Java concurrency API
* Jackson + Immutable (exercises on how to use both together)

## Considerations

This is a simple application used as my lab to practice the usage of some simple technologies as part of a code kata.
Most examples are contrived examples and should not be something to be used in production.
 