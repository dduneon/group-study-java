참조 http://web.mit.edu/6.005/www/fa15/classes/02-basic-java/
<br>

Objectives
- Learn basic Java syntax and semantics
- -----------------------------
- Transition from writing Python to writing Java
- ----------------------------------
<br>

# Language Basics

## Variables

- Object는 필드 state를 저장합니다
	- `int cadence = 0; int speed = 0; int gear = 1;`

What is Object?
- Field는 클래스 안에 있는 변수

```java
public class Main{
	static int static_Variable; // 클래스 변수
	int instance_Variable; // 인스턴스 변수

	void method(){
		int local_Variable; // 지역 변수
	}
}
```

| 변수          | 생성 시기                    | 소멸 시기       | 저장 메모리 |
| ------------- | ---------------------------- | --------------- | ----------- |
| 클래스 변수   | 클래스가 메모리에 올라갈때   | 프로그램 종료시 | 메서드 영역 |
| 인스턴스 변수 | 인스턴스 생성시              | 인스턴스 소멸시 | 힙 영역     |
| 지역 변수     | 블록 내 변수의 선언문 실행시 | 블록 벗어날 때  | 스택 영역   |

- __Instance Variables(Non-static Fields)__
	- 객체는 static 키워드 없이 선언된 필드 각각을 저장합니다.
- Class Variables(Static Fields)
	- Static을 사용해 선언된 필드
- Local Variables
	- 지역 변수는 선언된 메서드에만 표시되며 클래스의 나머지 부분에서는 접근 불가
- Parameters
	- 매개변수는 fields가 아닌 variables

-----------------------------------
<br>

# Snapshot diagrams

- __Snapshot diagrams__?
	- 실행중인 프로그램 stack(진행중인 메서드 , local variables)과 heap(현재 존재 하는 객체)의 내부 상태

## Primitive values
- bare constants로 표시된다. bare constants? 비어있는 상수
- 화살표는 변수 또는 객체 필드의 값에 대한 참조 이다 


![primitive values in snapshot diagram](http://web.mit.edu/6.005/www/fa15/classes/02-basic-java/figures/primitives.png)



## Object values
- 동그라미는 객체 값의 타입
- 더 자세한 내용은 필드 이름의 화살표가 가르키는 값

![object values in snapshot diagram](http://web.mit.edu/6.005/www/fa15/classes/02-basic-java/figures/objects.png)

----------------------------------------------

## Mutating values vs. reassigning variables

- 스냅샷 다이어그램을 사용해 변수 와 값의 변경 차이를 시각화 가능
- 변수또는 필드에 할당하면 화살표의 위치가 변경되어 다른 값 가르킨다
- 배열 또는 list와 같은 변동 가능한 값의 내용을 할당할때 값 내부의 참조를 변경
  

1. __Reassignment and immutable values__
- `String s = "a"; s = s + "b";`
![reassigning a variable](http://web.mit.edu/6.005/www/fa15/classes/02-basic-java/figures/reassignment.png)
- `String` is an example of an immutable type


2. __Mutable values__
- `StringBuilder`문자열을 나타내는 가변 객체이며, 객체의 값을 변경
```
StringBuilder sb = new StringBuilder ("A");
sb.append("B");
```
![mutating an object](http://web.mit.edu/6.005/www/fa15/classes/02-basic-java/figures/mutation.png)
Mutability and Immutability 차이는 안전한 코드에 역할을 한다

3. __Immutable references__
- `reference immutable`를 만들려면 `final`선언.
`final int n = 5;`

- java 컴파일러는 final변수를 런타임동안 한번만 할당.
- final variable은 런타임 동안 한번더 assigned을 못하게 하기 위해 static checking합니다.

![final reference is a double arrow](http://web.mit.edu/6.005/www/fa15/classes/02-basic-java/figures/final-reference.png)
- immutable reference(`final`) 이중 화살표
- immutable reference to mutable value ex)`final StringBuilder sb`
	- 같은 객체를 가르켜도 값은 바꿀수 있다
- mutable reference to immutable value ex)`String s`
	- 이미 존재하는 객체 이더라도 새로운 객체를 생성하여 재할당

-----------------------------------



<br>
# Java Collections
- Collections of objects : Java Collections Framework
-------------------------------------------

### List, Sets, and Maps

##### List
- List는 0개 이상의 객체가 순서로 나열

| java                     | description          |
| ------------------------ | -------------------- |
| `int count = list.size();` | 원소 개수를 센다     |
| `list.add(e);`| 요소의 끝에 추가한다 |
| `if(list.isEmpty())...`    | 리스트가 비었는지 체크                     |
<br>
##### Set
- set은 0개 이상의 중복이 없으며 순서가 없는 집합입니다

| java                 | description                        |
| -------------------- | ---------------------------------- |
| `s1.contains(e)`     | 집합에 e원소가 포함되어있는지 검사 |
| `s1.containsAll(s2)` | s1에 s2가 모두 담겨있는지 검사     |
| `s1.removeAll(s2)`   | s1에서 s2요소를 지운다            |

##### Map
- key와 value를 가진 hashable해야함

| java                   | description                     |
| ---------------------- | ------------------------------- |
| `map.put(key,val)`     | key가 val을 매핑한것을 추가한다 |
| `map.get(key)`         | key의 값을 구한다               |
| `map.containsKey(key)` | map에 key가 있는지 확인         |
| `map.remove(key)`      | key매핑을 지운다                                |

-----------------------------------
<br>

# Literals

- literal? - 데이터 그 자체 , 변수에 넣는 변하지 않는 데이터
	- `int a = 1;`  `int`앞에 `a`는 변수이고 `1`은 리터럴 이다

- 파이썬은 리스트 creating에 편리한 구문을 제공한다. 하지만 자바는 그렇지 않다
- 자바는 배열에 대한 literal구문을 제공
`String[] arr = {"a", "b", "c"};`
이것은 `list`가 아니라 배열을 만든다 
하지만 `utiliy function`을 사용해 `list`를 배열로 부터 만들수 있다
`Arrays.asList(new String[]{"a", "b", "c"})`

<br>
# Generics: declaring List, Set, and Map variables

- java 컬렉션을 사용하면 컬렉션에 포함할 객체의 타입을 제한할수있다
- 우리가 항목을 추가할때 컴파일러는 우리가 적절한 타입의 항목을 넣는지 static checking한다

```java
List<String> cities;  // a List of Strings
Set<Integer> numbers;  // a Set of Integers
Map<String,Turtle> turtles; // a Map with String keys and Turtle values
```
- Generic때문에 Primitive type은 들어갈수 없다

- Wrapper class  ? 
	- primitive type의 값을 갖는 객체
	- primitive type값을 내부에 두고 포장한다 해서 wrapper class
	- 내부에 있는 primitive type은 외부에서 변경 불가

- wrapper class 컬렉션을 쉽게 사용하기 위해 java는 `자동 형변환`을 수행
```
sequence.add(5); // add 5 to the sequence
int second = sequence.get(1); // get the second element
```
<br>
# ArrayLists and LinkedLists: creating Lists

`List`, `Set`, and `Map`은 인터페이스 이다
- 각각은 구현 코드를 제공하지 않는다.
- 다양한 상황에서 다양한 구현 선택 가능

```java
List<String> firstNames = new ArrayList<String>();
List<String> lastNames = new LinkedList<String>();
```
- 오른쪽 <> 내용 생략 가능

- `ArrayList` and `LinkedList`는 List의 구현부이다.
- `List`의 모든 동작을 구현한다
- 위의 코드에서 ArrayList 와 LinkedList를 바꿔도 문제가 없다.
- 의심스러울땐 `ArrayList`사용

# HashSets and HashMaps: creating Sets and Maps

`HashSet`은 기본적인 선택
`Set<Integer> numbers = new HashSet<>();`
 - 자바는 정렬된 TreeSet도 구현 가능하다
`Map<String,Turtle> turtles = new HashMap<>();`

# Iteration

- 아마 아래와 같다면
```java
List<String> cities        = new ArrayList<>();
Set<Integer> numbers       = new HashSet<>();
Map<String,Turtle> turtles = new HashMap<>();
```
- 도시,숫자,거북이, 등을 반복하는게 흔한 일

- 자바가 제공하는 구문 반복 List와 Set
```java
for(String city : cities){
	System.out.println(city);
}

for(int num : numbers){
	System.out.println(num);
}
```
- 자바가 제공하는 반복 Map
```java
for(String key : turtles.keySet()){
	System.out.println(key + ": " + turtles.get(key));
}
```
- index를 사용한 반복도 가능
```java
for(int i=0; i<cities.size(); i++){
	System.out.println(cities.get(i));
}
```
- 위 코드는 버그가 많으므로 웬만하면 피한다

# Java API documentation

- java api는 거의 모든 프로그래밍에 일반적인 유용한 도구이다
	- `java.lang.String` String타입의 객체를 사용하고 만든다
	- `java.lang.Integer` primitive wrapper class 자바는 자동으로 박싱 언박싱
	- `java.util.List` 파이썬과 다르게 자바에서는 구현이된다.
	- `java.util.Map`이하동문
	- `java.io.File` 파일을 읽을수 있는지 테스트, 삭제, 수정 시점 확인가능하다
	- `java.io.FileReader` 텍스트 파일을 읽게 해줍니다
	- `java.io.BufferedReader` 는 텍스트를 효율적으로 읽게 해주먀 한번에 한줄씩 읽는데 유용

공부를 할때 하다 모르면 가장 먼저 가야 할곳  JAVA API
