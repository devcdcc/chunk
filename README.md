# Introduction

This is the introduction document to the `foop` programming language, it will collect the main ideas, cases of study and thinking process.

# Case of study

Rust have been growing in popularize since its release from mozilla and have been most loved language in stakoverflow for 7 years in a row, it is because a high and low level language at the same time, and also because it implements an ownership memory model.

A LinkedIn [post](https://www.linkedin.com/posts/signify-technology_devs-rustlang-activity-7052200216506032128-WMbw)
asks their audience about considering using rust as their main programming language, half of people said yes or maybe,
and the other half saif definitely no or they will prefer another language.

Most of the people from previous poll may have a bias because signify is focused in recruiting functional programming developers.

FP is a niche of programming, but it is popular in that community. Most of those programming languages run over a virtual
machine, having a garbage collector, and even if it performs really well as scala, those languages are not well sited 
for near real time applications, things like video game engines, streaming voice/videos applications and some others 
couldn't run over a VM because it does do some garbage collector pauses that could affect the use experience.

The garbage collector could performs really well like in JVM, but it will have some spikes of memory
consumption because it does not release the memory at runtime, it is released periodically by the GC.

The main idea is to create a new programming language, this programming language should be multi paradigm, FP and OOP model 
compatible. Basically a very high level programming language, like scala or elixir but with an excellent performance,
using a ownership model like rust. 

# Languages requirements.

## Requirement #1 be a multi paradigm language.

Allow us to write OOP and FP programs, also must be immutable by default in order to be compatible with high concurrency.

## Requirement #2 Have an ownership model.

Should work without dealing with memory management, shouldn't deal with pointers either.

TODO: Check Lobster memory model.

## Requirement #3 Use stack by default.

Should give priority to stack using cache memory, and only use heap only for non-sized elements.
Primitives, strings, and data classes should be store into the stack too. 

TODO: Check alternative to Rust Sized interface.

## Requirement #4 Tail recursion compatible.

In order to be fully functional compatible, the compiler should be able to translate tail recursion functions into a 
simple do while like operation in order to reduce the number to variable into the stack.

## Requirement #5 Support higher kinded types.

Support higher kinded types in order to be compatible with generics and monads structures.

## Requirement #6 Should have simple syntax.

Reduce the number required of keywords, and reduce the types during definitions.

## Requirement #7 Must support implicit parameter.

Must support implicit parameter like given/using in scala.

## Requirement #8 Support IO operations.

In order to integrate to the computer hardware, our language must support IO operation to interact with computer sockets
and IO operations, including video operations.

TODO: Define the way to manage the bytes management in order to make easier the integration with external devices/ports.

## Requirement #8 Support pattern matching.
This pattern matching must be rich and powerful as scala or elixir.


# Language examples

Conditional

```scala
if a == 1 True else False
// otherwise
if a == 1
	True
else
	False

```

For / While should be functions based

```scala
do // monadic handler
	a = element.someStuff
	b <- monadLike 
	_ = println(b)
yield b

for element <- iterable
	// do some stuft
	???
yield element + 1 // optional map value

do currentValue <- initialValue while condition
	// do some stuff
	newInitialCondition = currentValue + 1
	yield newInitialCondition

do currentValue <- initialValue
	newInitialCondition = currentValue + 1
	yield newInitialCondition // lsat statement should yield by default, it not a value
while condition // x > 1 
```

Functions

```scala
fn sum(a: Int, b: Int): Int =
	val result = a + b
	result

sum(a: Int, b: Int): Int =
	result = a + b
	return result

sum(a: Int, b: Int) =
	a + b
sum(a, b): Int =
    a + b
sum(a, b) =
    a + b
```

Object oriented programming

```scala

trait Monad ?U: // generic type U
	map(U => ?V): Monand[V] // generic type V
	flatMap(U => Monad[?V]): Monand[V]
	flatMap(u: U => Monad[?V]): Monand[V] = 
		u(???) // ??? equals to unimplemented

trait Monad[U]: // generic type U
  map(U => ?V): Monand[V] // generic type V
    flatMap(U => Monad[?V]): Monand[V]
    flatMap(u: U => Monad[?V]): Monand[V] =
    u(???) // ??? equals to unimplemented

singleton None extends Monad _: // by context, where `?` means type validated in compilation type, By default Any.
	map(u => ?V): Self = self
	flatMap(_ => Monad[?V]) = self

	_apply(u: ?U): Monad[U] = MonadImpl(u)

class MonadImpl(u: ?U) extends Monad[U]:
class MonadImpl(u: ?U) extends Monad U:
	map(f: U => ?V) = MonadImpl(f(u))
	flatMap(f: U => Monad[?V]) = f(u)

```

Using mutable values inside a class.
```scala
class Person(
	name,
	protected var value: String = "default value could be here"
): // similar as Person[?U](name, value)
	setValue(value) = // implicit returns an Unit and receive and String value
		self.value = value
	setValue2(valueOne: String) =
		value = valueOne

person = Person(name = "Daniel", value = "122")
person.setValue("new value")

```