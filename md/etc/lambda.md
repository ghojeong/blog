# 5장 람다로 프로그래밍

코틀린 인 액션의 내용을 정리

## 5.1 람다식과 멤버 참조

### 5.1.1 람다 소개: 코드 블록을 함수 인자로 넘기기

자바에서는 **무명 내부 클래스** 를 통해 코드 블록을 넘길 수 있다.

특히 한 가지 메소드만 가진 인터페이스를 **SAM(Single Abstract Method, 단일 추상 메소드)** 인터페이스라고 한다.

자바의 **함수형 인터페이스** 는 전부 **SAM 인터페이스** 로 구현되어 있다.

예시

```java
public interface onClickListener {
  void onClick(View v);
}

button.setOnClickListener(v -> v.setText("텍스트가 바뀝니다."))
```

요약: JVM 위에서 람다식을 제대로 쓰려면, 무명 내부 클래스 혹은 익명 클래스를 익숙하게 쓸 수 있어야 한다.

### 5.1.2 람다와 컬렉션

요약: 컬렉션에서 제공하는 라이브러리 함수들을 달달 외워놓자.

### 5.1.3 람다 식의 문법

다음과 같이 코드를 간략화 할 수 있다.

```kotlin
people.maxBy({ p: Person -> p.age })
people.maxBy() { p: Person -> p.age }
people.maxBy { p: Person -> p.age }
people.maxBy { p -> p.age }
people.maxBy { it.age }
people.maxBy(Person::age)
```

### 5.1.4 현재 영역에 있는 변수에 접근

#### 변수 포획(Capture Variable)

람다 내에서 외부 변수에 접근하기 위해 레퍼런스를 사용한다.

즉 변수를 Wrapper Class 로 감싸서, 레퍼런스를 통해 참조한 객체의 내부 멤버를 바꾼다.

예시

```kotlin
var clicks = 0

fun tryToCountButtonClicks(button: Button): Int {
  button.onClick { clicks++ }
}
```

안드로이드에서 이런 코드를 많이 한다.

매우 함수형스럽지 않은, 자바스러운 코드가 되었다.

변수 포획을 활용하면 반드시 var 를 쓰게 된다.

val 로 통일하기 위해서라도, 변수 포획을 지원하지 말아야 했다고 생각한다.

내 안드로이드 팀원들은, 이런게 있는지도 모르는게 하고 싶다.

### 5.1.5 멤버 참조

Person::age 에서 :: 를 **멤버 참조** 라고 부른다.

::Person 을 통해 Person 의 **생성자 참조** 를 할 수 있다.

```kotlin
val createPerson = ::Person
val p: Person = createPerson(name = "Pyro")

val getName = p::name
println(getName())
```

위에서 p::name 을 **바운드 맴버 참조(Bound Member Reference)** 라고 한다.

## 5.2 컬렉션 함수형 API

함수형 API 라는 표현을 쓰지만, 나는 함수형 [**Operator**](https://rxjs.dev/guide/operators) 라는 표현을 더 좋아한다.

[RxMarbles](https://rxmarbles.com/) 에서 직관적으로 학습을 해보자.

하나하나 다 보면 졸리니까, 필요할 때마다 찾아서 공부한다.

### 5.2.1 필수적인 함수: filter와 map

- filter: 컬렉션에서 predicate 이 true 인 element 를 뽑아낸다.
- map: 컬렉션의 각 element 를 변형한다.

맵 자료형의 경우 filterKeys, filterValues, mapKeys, mapValues 라는 게 존재한다.

하나하나 다 외우려고 하면 힘드니깐, 대충 넘어가자.

### 5.2.2 all, any, count, find: 컬렉션에 술어 적용

- all: 컬렉션에서 모든 element 가 predicate 을 만족하는가?
- any: 컬렉션에서 아무 element 가 predicate 를 만족하는가?
- count: 컬렉션에서 predicate 를 만족하는 element 가 몇개있는가?
- find: 컬렉션에서 predicate 를 만족하는 element 를 찾는다.

```kotlin
listOf(1,2,3,4).find { it == 6 }
  ?.let { print(it) }
  ?: kotlin.run { print("No Number") }
```

### 5.2.3 groupBy: 리스트를 여러 그룹으로 이뤄진 맵으로 변경

SQL 의 GROUP BY 랑 비슷하다. 쿼리 짜는 기분으로 사용하면 된다.

### 5.2.4 flatMap 과 flatten: 중첩된 컬렉션 안의 원소 처리

리스트 안에 리스트들이 있을 경우, 이를 하나의 리스트로 합치기 위해 사용한다.

보통 groupBy 로 묶어서 이것저것 연산을 한 후에, 다시 하나의 리스트로 복원하기 위해 사용한다.

참고로 리액티브 프로그래밍을 하면, [FlatMap vs SwitchMap vs ConcatMap](https://softwaree.tistory.com/30) 이 어떻게 다른지 물어보는 면접 질문이 많이 나온다.

## 5.3 지연 계산(lazy) 컬렉션 연산

asSequence 문법을 쓰지 않으면 결과 컬렉션이 바로 생성된다. : **Eager Evaluation**

asSequence 문법을 쓰면 **Lazy Evaluation** 을 한다.

asSequence() 랑 자바의 stream() 이랑 똑같다. 다만 asSequence 가 stream 보다 먼저 나왔다.

### 5.3.1 시퀀스 연산 실행: 중간 연산과 최종 연산

asSequence 를 하고 나서 만들어진 sequence 는 toList 와 같은 **최종(Terminal) 연산** 을 하기 전에는 계산이 수행되지 않는다.

최종 연산 전의 연산들을 **중간(Intermediate) 연산** 이라고 한다.

### 5.3.2 시퀀스 만들기

asSequence 로 리스트를 시퀀스를 만들지 않고,

**generateSequence** 문법을 통해 시퀀스를 로직으로 만들 수도 있다. (예시: 피보나치 수열)

관련 면접 질문: Hot Observable vs Cold Observable

## 5.4 자바 함수형 인터페이스 활용

자바의 함수형 인터페이스(Functional Interface) 란,
곧 SAM(Single Abstract Method, 단일 추상 메서드) 인터페이스이다.

### 5.4.1 자바 메서드에 람다를 인자로 전달

람다식(Labmda Expression) 을 함수 인자로 넘기면,
람다식을 함수형 인터페이스 익명 클래스 객체로 만들어서 넘긴다.

익명 클래스 내부 로직에서, 클래스 외부 변수 값을 참조할 때는 변수 포획(Capture Variable) 기법을 사용한다.

### 5.4.2 SAM 생성자: 람다를 함수형 인터페이스로 명시적으로 변경

코틀린의 람다식을 자바의 함수형 인터페이스로 바꾸고 싶을 때는 SAM 생성자로 감싸야 한다.

아래 코드에서는 `Runnable { println() }` 을 통해 println 을 Runnable SAM 생성자로 감쌌다.

```kotlin
fun createAllDoneRunnable(): Runnable {
  return Runnable { println("Hello") }
}

createAllDoneRonnable().run()
```

솔직히 그냥 코틀린 람다로 통일하면 볼일이 없는 코드다.

Runnable SAM 생성자로 객체를 만들 수 있다는 사실을 머릿속에서 지워버리자.

Runnable 객체를 직접 넘기기 보다는, 람다식을 직접 넘기는 것을 개인적으로 선호한다.

몇몇 분들은 Runnable 혹은 Listener를 싱글톤으로 만들어서 메모리 절약을 할 수 있다고 우기겠지만,
내 경험상 그런 분들은 객체의 생명주기를 제대로 인지 못하고 코딩해서 메모리 문제를 발생시켰을 가능성이 크다.

생성자 참조 혹은 리플렉션 참조 람다를 프레임워크에게 넘겨서, 프레임워크가 메모리를 관리할 수 있도록 하자.

## 5.5 수신 객체 지정 람다: with와 apply

[apply vs with vs let vs also vs run](https://medium.com/@limgyumin/%EC%BD%94%ED%8B%80%EB%A6%B0-%EC%9D%98-apply-with-let-also-run-%EC%9D%80-%EC%96%B8%EC%A0%9C-%EC%82%AC%EC%9A%A9%ED%95%98%EB%8A%94%EA%B0%80-4a517292df29)

그냥 한번이라도 직접 사용해보는게 이해가 더 잘 되는 것 같다. 굳이 책으로 공부하지 말자.

### 5.5.1 with 함수

binding 을 통해 view 를 가져오려고 할 때 개인적으로 많이 쓴다.

AS-IS

```kotlin
binding.button0.setOnClickListener {
    expression += 0
    binding.textView.text = expression.toString()
}
binding.button1.setOnClickListener {
    expression += 1
    binding.textView.text = expression.toString()
}
binding.button2.setOnClickListener {
    expression += 2
    binding.textView.text = expression.toString()
}
```

TO-BE

```kotlin
with(binding) {
    button0.setOnClickListener {
        expression += 0
        textView.text = expression.toString()
    }
    button1.setOnClickListener {
        expression += 1
        textView.text = expression.toString()
    }
    button2.setOnClickListener {
        expression += 2
        textView.text = expression.toString()
    }
}
```

### 5.5.2 apply 함수

개인적으로 sequence 혹은 stream 에서 시스템 콜 혹은 프레임워크 함수 콜을 통해 side effect 를 발생시키고 싶을 때 많이 쓴다.

아래에서는 println 을 썼지만, 스트림 과정 중에 디버깅을 위해 로깅을 하는 용도로 매우 유용하다.

스트림 중간에 프록시 서버 로직을 집어 넣는다는 느낌으로 쓰면 편하다.

```kotlin
fun calculate(input: String): Int {
    val list = input.split(" ")
    return list.subList(1, list.size)
        .apply { println(toString()) }
        .chunked(2)
        .apply { println(toString()) }
        .map { Pair(IntArithmetics.from(it[0]), it[1].toInt()) }
        .apply { println(toString()) }
        .fold(list[0].toInt()) { acc, curr -> curr.first.apply(acc, curr.second) }
}
```
