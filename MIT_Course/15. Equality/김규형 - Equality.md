# Equality(23.10.13)

### Introduction

 추상화 함수에서 equality 연산을 정의 하는 방법

## ****Three Ways to Regard Equality****

- equality를 바라보는 다양한 방법
- **추상화 함수 사용(Using an abstraction function)**
    - 추상화 함수 $F$ 를 정의해 $F(a) = F(b)$ 인 경우에만 equality를 확정
- **관계를 이용(Using a relation)**
    - equilvalence(동치) E ⊆ T X T 아래와 같다.
        - reflexive(반사): E(t,t) ∀ t ∈ T
        - symmetric(대칭): E(t,u) ⇒ E(u,t)
        - transitive(추론): E(t,u) ∧ E(u,v) ⇒ E(t,v)
    - E의 equality를 정의를 사용한다면 우리는 오직 E(a,b) 일때만 equals하다고 말 할수 있다.
- 위는 추상화 함수로 유도된 동등성 관계이다.
- **Using observation(관찰 이용)**
    - 호출해서 직접 관찰하기
        - {1,2}| = 2 and |{2,1}| = 2
        - 1 ∈ {1,2} is true, and 1 ∈ {2,1} is true
        - 2 ∈ {1,2} is true, and 2 ∈ {2,1} is true
        - 3 ∈ {1,2} is false, and 3 ∈ {2,1} is false
        - … and so on

### Example: Duration

```java
public class Duration {
    private final int mins;
    private final int secs;
    // rep invariant:
    //    mins >= 0, secs >= 0
    // abstraction function:
    //    represents a span of time of mins minutes and secs seconds

    /** Make a duration lasting for m minutes and s seconds. */
    public Duration(int m, int s) {
        mins = m; secs = s;
    }
    /** @return length of this duration in seconds */
    public long getLength() {
        return mins*60 + secs;
    }
}
```

- equal 한가?

```java
Duration d1 = new Duration (1, 2);
Duration d2 = new Duration (1, 3);
Duration d3 = new Duration (0, 62);
Duration d4 = new Duration (1, 2);
```

### ****== vs. equals()****

- Java가 가지고 있는 동등성 연산
    - `==` 참조를 비교 한다. (참조 평등을 테스트) , 메모리의 같은 저장소를 가르킨다면 동일하다.
    - `equals()` 개체의 내용의 동일성

## ****Equality of Immutable Types****

- `equals` 의 정의

```java
public class Object {
    ...
    public boolean equals(Object that) {
        return this == that;
    }
}
```

- 기본적으로 `==` 와 동일하다.
- 그런데 immutable 타입도 이렇게 할수 있을까…?
- `Duration` 의 `equlas`

```java
public class Duration {
    ...
    // Problematic definition of equals()
    public boolean equals(Duration that) {
        return this.getLength() == that.getLength();
    }
}
```

- 해당 코드의 반례
    
    ```java
    Duration d1 = new Duration (1, 2);
    Duration d2 = new Duration (1, 2);
    Object o2 = d2;
    d1.equals(d2) → true
    d1.equals(o2) → false
    ```
    
    - 오버로드의 발생으로 위의 메소드가 실현되버린다.
- `equlas` 를 표현한다면?
    
    ```java
    public class Duration extends Object {
        // explicit method that we declared:
        public boolean equals (Duration that) {
            return this.getLength() == that.getLength();
        }
        // implicit method inherited from Object:
        public boolean equals (Object that) {
            return this == that;
        }
    }
    ```
    
    - Static checking에서 자바 컴파일러는 매개변수에 따라 메소드 오버로딩을 실행
    - `d1.equals(o2)` 와 `d1.equals(d2)` 는 같은 객체를 가르키지만 평등성이 어긋나는 예
- 개선된 `equals()`
    
    ```java
    @Override
    public boolean equals (Object thatObject) {
        if (!(thatObject instanceof Duration)) return false;
        Duration thatDuration = (Duration) thatObject;
        return this.getLength() == thatDuration.getLength();
    }
    ```
    
    - 해결
    
    ```java
    Duration d1 = new Duration(1, 2);
    Duration d2 = new Duration(1, 2);
    Object o2 = d2;
    d1.equals(d2) → true
    d1.equals(o2) → true
    ```
    

## Object Contract

- `Object` 클래스의 명세는 중요하다.
- equals 를 정의할때 `Object` 의 `equals` 를 오버로딩 하는 방식으로 진행된다.
    - `equals` 의 동치 관계를 정의해야한다. → 반사적, 대칭적, 전이적 관계의 정의
    - `equals` 가 일치해야한다. 메소드의 반복 호출에도 항상 같은 결과를 반환
    - `x.equals(null)` 는 언제나 false를 반환해야한다.
    - `hasCode` 는 `equals` 에 대해 언제나 같은 값을 생산하다.
    

## ****Breaking the Equivalence Relation****

- `equlas()` 를 정의 한다면 동치 관계(반사, 대칭, 전이)부터 시작되어야한다.
- 그렇지 않은 경우 동일성은 불규칙적으로 예측 불가능하게 동작한다. → a와 b는 동등하다. 하지만 b는 a와 동등하지 않는다는 결과
    
    ```java
    private static final int CLOCK_SKEW = 5; // seconds
    
    @Override
    public boolean equals (Object thatObject) {
        if (!(thatObject instanceof Duration)) return false;
        Duration thatDuration = (Duration) thatObject;
        return Math.abs(this.getLength() - thatDuration.getLength()) <= CLOCK_SKEW;
    }
    ```
    

### Breaking Hash Tables

- `hashcode` 가 어떻게 작동하는지 어느정도 이해해야한다.
- `HashSet` , `HashMap` 은 `HashCode` 에 의존하여 hashtable 을 사용하여 데이터를 저장한다.
- 메소드는 객체를 저장하고 map 에서 key를 사용해 구현한다.
- 해쉬 테이블은 mapping으로 나타내는데 key로 값을 매핑한다.
- 다른 트리 보다 우수함
- key는 `equals` 나 `hashcode` 를 제외하고 특정 속성을 가질 필요가 없다.
- 저장할 객체가 생기면 key의 hashcode를 계산하고 배열범위의 인덱스로 변환한뒤에 삽입
- hashtable의 표현 불변성은 결정된 값에 키가 있다는 제약조건이 있다.
- hash 코드는 키가 인덱스 전체에 퍼지도록 설정, 충돌 발생시 각자 처리
- `Object` 는 동일한 개체가 동일한 hashcode를 가져야한다.
- `hashcode()` 를 활용한 `equals`
    
    ```java
    private static final int CLOCK_SKEW = 5; // seconds
    
    @Override
    public boolean equals (Object thatObject) {
        if (!(thatObject instanceof Duration)) return false;
        Duration thatDuration = (Duration) thatObject;
        return Math.abs(this.getLength() - thatDuration.getLength()) <= CLOCK_SKEW;
    }
    ```
    
- 불변 객체 같은 경우 다른 구현이 필요하다.
    
    ```java
    Duration d1 = new Duration(1, 2);
    Duration d2 = new Duration(1, 2);
    d1.equals(d2) → true
    d1.hashCode() → 2392
    d2.hashCode() → 4823
    ```
    
    - 당연히 다른 객체니 `hashcode` 는 다르다.
- 이런 경우의 eqaul성은 어떻게 수정해야할까
- 가장 합리적인 방법 hashcode()를 `@Override` 한다. → 나의 equals 한 추상 함수로
    
    ```java
    @Override
    public int hashCode() {
        return (int) getLength();
    }
    ```
    
    - 성능이 안좋은 해쉬 함수라도 부정확한 함수보다는 낫다.

> `equals` 를 override 할 때에는 항상 `hashCode` 를 override 할것
> 

## Equality of Mutable Types

- mutable 한 친구들의 equality는?
- 가변 객체의 equality는 두가지로 나뉜다.
    - 상태가 바뀌지 않고 관찰자로 구분할 수 없을 때 observational equality(관찰 평등) 이라고 불린다.
    - 상태가 바뀜(돌연변이)이며 관찰자로 구분 할수 없을때 bejavioral equality(행동 평등) 이라고 불린다.
- immutable의 경우 일반적으로 관찰과 행동이 동일하다.
- 관찰 동일성은 편하지만 버그가 잠재될 가능성이 있다.
    
    ```java
    List<String> list = new ArrayList<>();
    list.add("a");
    
    Set<List<String>> set = new HashSet<List<String>>();
    set.add(list);
    ```
    
    - 이러한 상황에서 아래와 같은 결과
    
    `set.contains(list) → true`
    
    `list.add("goodbye");`
    
    `set.contains(list) → false!` → 더이상 equal 하지 않음
    
    ```java
    for (List<String> l : set) {
        set.contains(l) → false!
    }
    ```
    
    - 이런식으로 반복한다면 상황은 더욱 심해진다.
    - *list에 객체가 추가됨으로써 내부주고자 변경된다.(내부요소의 해시코드 기반이기에)* hashcode가 변경된다.
- `java.util.Set` 의 명세
    
    > *Note: Great care must be exercised if mutable objects are used as set elements. The behavior of a set is not specified if the value of an object is changed in a manner that affects equals comparisons while the object is an element in the set.*
    > 
- 이러한 주의를 명시하고 immutable 한 객체를 사용하는것도 방법
- 이러한 상황에서는 **행동 평등** 의 구현의 필요성

## ****The Final Rule for equals and hashCode()****

- Immtable 타입의 경우
    - `equals()` 는 추상적인 값을 비교해야합니다. ( 행동 평등 구현 )
    - `hashCode()` 는 추상 값으로 매핑해야한다.
- Mutable 타입의 경우
    - `equals()` 의 경우 참조를 비교해야한다. ( 행동 평등 구현 )
    - `hashCode()` 도 참조 값으로 매핑해야한다.

### Autoboxing and Equality

- Object 형과 원시형의 동등성은…?

```java
Integer x = new Integer(3);
Integer y = new Integer(3);
x.equals(y) → true
x == y -> false
(int)x == (int)y // returns true
```

- 이런 버그를 조심하자
    
    ```java
    Map<String, Integer> a = new HashMap(), b = new HashMap();
    a.put("c", 130); // put ints into the map
    b.put("c", 130);
    a.get("c") == b.get("c") → ?? // what do we get out of the map?
    ```
    
    - 둘다 결과물이 `Integer` 라서 false가 나온다!

## Summary

- **Equality** 는 동치 관계(반사, 대칭, 추이) 여야한다.
- **Equality** 는 hashCode와 일치해야한다. 그래야 제대로 작동 한다.
- 추상화 함수는 불변 데이터 형의 **Equality** 에 기초로 만들어진다.
- 참조 **Equality** 는가변 데이터 유형에서 일관성있게 보장하는 방식이다.
