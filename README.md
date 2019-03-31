# Experiments

I am using this repository to add some simple experiments I've been writing as Code Katas
in order to get in shape with important concepts that I believe a backend developer
must master. Of course there are other things that are important and I won't have all
important examples here. Some concepts are very basic topics which can potentially
lead myself to stump in the future if not practicing.

I like the idea of running Code Katas in order to practice when I am not actually
working. Some projects and ideas here are inspired by Dave Thomas pages at [Codekata](http://codekata.com/). I still don't have practiced any of Dave Thomas katas, because I want to practice things
that I judge important at the moment.

> **Disclaimer**
> This is a personal repository and I am saving code here as well as the descriptions
> of my own katas. It's for personal reference only.

## List of Katas

### A payment API that has to do asynchronous processing

* **Purpose**: practice asynchronous techniques using Spring Web + Concurrency API (Future & CompletableFuture).
* **Sample project**: /asyncexperiments

Create a payment REST API that allows any client to:
- Get a list of payments;
- To send a payment to be processed. Take a look at the following data structure to be used:

```java
@Data
public class Payment {
    private PaymentMethod paymentMethod;
    private int amountInCents;
    private Date dueDate;
    private Locale locale;
}
```
Validates if the payment method sent is available in the locale sent (if not, throw an exception
inside an ascynchronous void method and make sure that you can handle this exception).

- Create an endpoint that allows to send a payment to be asynchronously processed so that the result
is sent as a callback to a fictitious endpoint (use request bin to see the results).

### votolab

This simple project, is a simulation of an application that allows for voting.
It provides an endpoint to send votes to some sort of subject in order to be asynchronously processed.
Each vote is sent to a standard SQS Queue backed by a simple consumer written from scratch using only AWS SDK and Executor framework from Java concurrency API.

Simply put it is a producer/consumer example using Amazon SQS

**Tecnhologies**

* AWS SQS
* Framework executor of Java concurrency API
* Jackson + Immutable (exercises on how to use both together)

