# Basic Java

### Getting started with the Java Tutorials

→ 이 단원에서는 공식 Java API 문서를 자주 참조함

**Questions and Exercies: Variables**

1. Instance variable의 또 다른 이름은? - `non-static field`

2. class variable의 또 다른 이름은? - `static field`

3. local variable는 일시적으로만 저장된다. 어디에 저장되는가? `method`

4. method signature안에 있는 괄호에 선언된 변수를 뭐라고 부르는가? `parameter`

5. Java 프로그래밍 언어가 지원하는 8가지 Primitive Type은 무엇인가?

→ `byte`, `short`, `int`, `long`, `float`, `double`, `char`, `boolean`

6. 문자열은 무슨 클래스로 표현되는가? - `java.lang.String`

7. 고정된 개수의 값을 보유하는 길이가 변하지않는 컨테이너 개체는? - `Array`

---

**Questions and Exercies: Operators**

1.

```java
**arrayOfInts[j] > arrayOfInts[j+1]**
**// 코드에는 어떤 연산자가 포함되어 있나? > , +**
```

2.

```java
int i = 10;
int n = i ++ & 5;
// a. 코드가 실행된 후의 i와 n의 값은 무엇인가? i = 11, n = 0
// -> 5 = 0101, 10 = 1010 ->  0000, 11 = 1011
// b. i의 최종 값은 무엇이며 prefix 연산자가 아니라 postfix 연산자를 사용하는 이유는?
// -> i의 최종값 11 Prefix 연산자를 사용했다면 10이 아닌 11값을 이용하기 때문에
// 만약 postfix 연산자를 사용했다면 n은 1이다.
```

1. **위에서 a 값을 반전하기 위해서 어떤 연산자를 사용해야하는가? !**
2. **두 값을 비교하는 데 사용되는 연산자는 무엇입니까? ==** 

1. **다음 코드를 해석하세요**

```java
result = someCondition ? value1 : value2 ;
/*
if(someCondition == true){
	result = value1;
}else{
	result = value2;
}
*/
```

1. **다음을 복합할당으로 바꾸세요**

```java
class ArithmeticDemo {

     public static void main (String[] args){
          
          int result = 1 + 2; // result is now 3
          System.out.println(result);

          result = result - 1; // result is now 2
          System.out.println(result);

          result = result * 2; // result is now 4
          System.out.println(result);

          result = result / 2; // result is now 2
          System.out.println(result);

          result = result + 8; // result is now 10
          result = result % 7; // result is now 3
          System.out.println(result);

					int result = 3;
					result -= 1;
					result *= 2;
					result /= 2;
					result += 8;
					result %= 7;
     }
}
```

1. **다음 코드에서 6이 두번 출력되는 이유를 설명하세요**

```java
class PrePostDemo {
    public static void main(String[] args){
        int i = 3;
        i++;
        System.out.println(i);    // "4"
        ++i;                     
        System.out.println(i);    // "5"
        System.out.println(++i);  // "6"
        System.out.println(i++);  // "6" -> Postfix 연산자이므로 6이 출력된 후 1이 증가
        System.out.println(i);    // "7"
    }
}
```

---

**Questions and Exercises: Expressions, Statements, and Blocks**

1. **연산자는 값을 계산하는 ____를 구축하는데 사용될 수 있다. - Expression**
2. **Expression는 ____의 핵심 구성요소이다. - Statement**
3. **Statement는 ____의 그룹이다. - blocks**
4. **이 코드는 ___Expression의 예시이다. - compound**

```java
1 * 2 * 3;
```

1. **Statement는 자연어문장과 거의 동일하지만 마침표로 끝나는 대신 ___로 끝난다. - semicolon(;)**
2. **Block는 균형잡힌 ____ 사이에 있는 0개 혹은 그 이상의 statements 모음이며 단일 명령문이 허용되는 모든 곳에서 사용할 수 있다. → () (braces)**

```java
double aValue = 8933.234; // assignment statement
aValue++; // increment statement
System.out.println("Hello World!"); // method invocation statement
Bicycle myBike = new Bicycle(); // object creation statement
```

---

## Snapshot diagrams

→ 애매한 질문을 이해하기 위해서  런타임에 일어나는 일을 그리는데 유용하다.

- `Snapshot diagrams` 은 런타임 중 내부 상태를 표시한다.

**→ Stack : 진행중인 메서드와 해당 지역변수**

**→ Heap : 현재 존재하는 Object**

**6.005에서 스냅샷 다이어그램을 사용하는 이유**

1. 그림을 통해 서로 이야기하기 위해서
2. Primitive type과 object의 유형, 불변 값과 불변 참조, 추상화 등 개념을 설명을 쉽게 하기 위해
3. 등등

**Primitive values**

→ Primitive values는 순수 상수로 표시된다. 화살표는 변수또는 개체값을 참조한다. 

![Untitled](../image/Untitled.png)

****Object values****

→ Object value는 원에 감싸져서 표시가 된다. 

→ 만약 좀 더 세부적인 것을 알고 싶다면 내부 이름을 보면된다.

→ 더 세부적인 내용을 보기 위해서는 내부에서 선언한 유형을 보면된다.

![Untitled](Basic%20Java%20b4b5f2efcc3d416cbcfeea327954f831/Untitled%201.png)

**Mutating values vs. reassinging variables**

**→ 스냅샷 다이어그램은 변수를 바꾸는 것과 변수의 값을 바꾸는 것의 구분을 시각화해준다.**

- 만약 변수나 필드에 할당한다면 변수의 화살표는 다른 값을 가르킨다.
- 배열이나 목록 같은 변경 가능한 값의 내용에 할당하면 내부의 참조값이 바뀐다.

****Reassignment and immutable values****

→ `String` 은  불변 타입이다. 즉, 값이 생성되면 절대 변경할 수 없는 유형임

immutable objects(불변 타입)은 스냅샷 다이어그램에서는 이중테두리로 표시된다.

![Untitled](Basic%20Java%20b4b5f2efcc3d416cbcfeea327954f831/Untitled%202.png)

****Mutable values****

→ `String`과는 다르게 `StringBuilder`은 문자열 변경이 가능한 가변적인 객체이다.

![Untitled](Basic%20Java%20b4b5f2efcc3d416cbcfeea327954f831/Untitled%203.png)

****Immutable references****

**→ 자바에서는 또한 immutable references를 지원한다. 한번 할당하면 다시 재 할당 할 수 없다. `final`이라는 키워드를 사용한다.**

**→ `final`이라는 키워드를 사용한 변수에 한번 더 할당하게 된다면 컴파일에러가 발생한다. 즉 , 자바에서 static checking를 지원해준다.**

- **스냅샷 다이어그램에서는 final value는 이중화살표로 표시된다.**

**→ `immutable reference`일지라도 `mutable value`를  가질 수 있다.(ex. final StringBuilder sb)**

**→ 또한 `mutable reference`일지라도 `immutable value`를 가질 수 있음(ex. String)**

---

## Java Collections

**→ 고정된 길이의 primitive type원소를 저장하는 arrays보다 더 유연하고 유용한 Collections FrameWork가 있다.**

### Lists, Sets, and Maps

---

**List**

**→ 자바의 `List`는 파이썬의 리스트와 유사하다. List에는 0또는 그 이상의 `object`를 넣을 수 있음**

![Untitled](Basic%20Java%20b4b5f2efcc3d416cbcfeea327954f831/Untitled%204.png)

- **스냅샷 다이어그램에서 List는 인덱스가 있는 객체로 나타낸다.**

![Untitled](Basic%20Java%20b4b5f2efcc3d416cbcfeea327954f831/Untitled%205.png)

**Set**

**→ 자바에서 `Set`는 0또는 그이상의 unique object가 담겨있다. 파이썬의 set과 유사하다.**

![Untitled](Basic%20Java%20b4b5f2efcc3d416cbcfeea327954f831/Untitled%206.png)

- **스냅샷 다이어그램에서 Set는 이름이 없는 필드로 나타낸다.**

![Untitled](Basic%20Java%20b4b5f2efcc3d416cbcfeea327954f831/Untitled%207.png)

**Map**

→ 자바의 `Map`은 파이썬의 dictionary와 유사하다. 파이썬처럼 `Key` 값으로 `Value` 값을 찾아냄

![Untitled](Basic%20Java%20b4b5f2efcc3d416cbcfeea327954f831/Untitled%208.png)

- **스냅샷 다이어그램에서는 Map은 키/값 쌍을 포함하는 객체로 표시가 된다.**

**Generics: declaring List, Set, and Map variables**

**→ 파이썬과 달리 자바는 Collections에 들어가는 object를 제한할 수 있음  Item을 넣을 때 컴파일러가 static checking를 하면서 적절한 타입이 들어가는지 검사한다.**

```java
List<String> cities;        // a List of Strings
Set<Integer> numbers;       // a Set of Integers
Map<String,Turtle> turtles; // a Map with String keys and Turtle values
```

wraper types를 collections에서 사용하기 쉽게 만들기 위해서 자동으로 변환해준다.

만약 `List<Integer> sequence` 로 선언했다면

```java
sequence.add(5);              // add 5 to the sequence
int second = sequence.get(1); // get the second element
// int 값을 넣고 int로 받아도 자동으로 변환해준다.
```

### ArrayLists and LinkedLists: creating Lists

**→ 자바는 type을 구별하는 것을 도와준다. 무엇을 할지는 구현을 통해서 한다.**

- `**List`, `Set`, `Map` 모두 인터페이스이다. 무엇을 작동해야하는지를 정의하지만 코드를 제공해주지 않는다.**

List를 구현하는 방법

```java
List<String> firstNames = new ArrayList<String>();
List<String> lastNames = new LinkedList<String>();
// -> 은지네릭 타임이 왼쪽과 오른쪽이 같다면 적지 않아도 된다.
List<String> firstNames = new ArrayList<>();
List<String> lastNames = new LinkedList<>();
```

**→ `ArrayList` 와 `LinkedList` 모두 `List`를 구현하는 것이므로 List와 같은 작동을 한다. 그리고 List에 명시된대로 작동해야한다. 예를 들어 `firstNames`와 `lastName`는 같은 방식으로 작동하기 때문에 `ArrayList`와 `LinkedList`를 반대로 넣어주어도 코드는 작동한다.**

→ 성능을 신경써야하지만 헷갈릴 경우 ArrayList를 넣어야한다.

<aside>
💡 **`ArrayList`는 배열 형태로 구성되어 데이터 검색시 유리함**

</aside>

<aside>
💡 **`LinkedList`는 노드 형태로 구성되어 데이터 추가, 삭제에 유리함**

</aside>

### HashSet and HashMap: creating Sets and Maps

**→ `HashSet`은 `Set` 의 기본 선택이다.**

**→ `Set`에는 또한 정렬된 sets형태인 TreeSet도 제공한다.**

### HashSet<E> class

- 가장 많이 사용하는 Set 클래스
- 해시 알고리즘을 사용하여 검색속도가 매우 빠름

`**해시 알고리즘(hash algorithm)` : 해시 함수를 사용하여 데이터를 해시 테이블에 저장하고 그것을 다시 검색하는 알고리즘**

→ 자바에서 해시 알고리즘을 이용한 자료 구조는 배열과 연결 리스트로 구현된다.

![Untitled](Basic%20Java%20b4b5f2efcc3d416cbcfeea327954f831/Untitled%209.png)

### TreeSet<E> class

- **데이터가 `정렬`된 상태로 저장되는 `이진 검색 트리(binary search tree)`의 형태로 요소를 저장한다.**
- **`Set` 인터페이스를 구현하므로, 요소를 순서에 상관없이 저장하고 중복된 값은 저장하지 않는다.**

**→ HashSet과 TreeSet의 차이점**

- Set이란 순서가 없는 집합체이며, 중복을 허용하지 않음
- `TreeSet`은 `HashSet`과 다르게 그 값이 정렬되어 저장된다. → 그렇기 때문에 `HashSet`보다 속도가 느림

**Map의 기본 선택은 HashMap이다.**

---

### Iteration

```java
List<String> cities        = new ArrayList<>();
Set<Integer> numbers       = new HashSet<>();
Map<String,Turtle> turtles = new HashMap<>();
```

→ 자바는 파이썬과 비슷한 방법으로 `List` 나 `Set` 를 돌면서 Item을 꺼내온다.

**ex) List, Set**

```java
for (String city : cities) {
    System.out.println(city);
}

for (int num : numbers) {
    System.out.println(num);
}
```

Map은 keySet()을 받아와서 for문을 돌 수 있다.

**ex) Map**

```java
for (String key : turtles.keySet()) {
    System.out.println(key + ": " + turtles.get(key));
}
```

---

## Java API documentation

→ 자바에는 방대한 양에 유용한 API을 찾을 수 있다.

`**API` : Application Programming interface 이다.**

- `**java.lang.String` : String의 풀네임이다. 우리는 String type를 단지 `“double quotes”`를 통해 만들 수 있다.**
- `**java.lang.Integer` : 기본 wrapped 클래스이다. 자바는 대부분 상황에서 자동으로 primitive를 wrapper클래스로 감싸준다.(boxed)**
- `**java.util.List`: 파이썬 리스트와 비슷하다. 파이썬에서는 언어의 일부이지만 자바는 구현됨**
- `**java.util.Map`: 위와 같음**
- `**java.io.File` : 디스크의 파일을 나타냄. 파일을 읽을 수 있는지 확인하고 파일을 삭제하고 마지막 수정일자를 확인 할 수 있음**
- `**java.io.FileReader` : 텍스트 파일을 읽을 수 있음**
- `**java.io.BufferedReader` : 텍스트를 효율적으로 읽을 수 있으며 한번에 한 줄 읽는 기능도 제공한다.**

→ 자바 API에 포함된 것

- **The method signature: 리턴 타입, 메소드 이름, 그리고 파라미터를 확인 할 수 있다. 또한 발생하는 예외 또한 발견 할 수 있다.**
- **The description: API 설명서**
- **Parameters : 메소드 인자에 대한 설명**
- **마지막으로 리턴타입에 대한 설명**
