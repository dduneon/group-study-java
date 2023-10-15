# Equality

## **Objectives**

- **Abstract function, equivalence relation 그리고 observations 측면에서 정의된 equality를 이해한다.**
- **reference equality와 object equality의 차이점을 이해한다.**
- `**mutable type` 에 대한 엄격한 equality를 구별한다.**
- **객체 계약을 이해하고 `mutable type` 과 `immutable type`에 대해 equality를 올바르게 구별 할 수 있다.**

---

## **Introduction**

**이전에서 우리는 `representation` 이 아니라 `operations`로 특정지어지는 type을 만들어 date abstraction에 대한 엄격한 개념을 개발했다.** 

**Abstract data type의 경우 추상화 함수는 representation value를 abstract type의 값으로 해석하는 방법을 설명하고, 추상화 함수의 선택에 따라 각 ADT 작업을 구현하는 코드를 작성하는 방법이 어떻게 결정되는지 보았다.**

**이번 단원에서는 data type에서 값의 equality를 정의하는 방법에 대해서 알아보겠다. 추상화 함수는 ADT에서 equality operation을 명확하게 정의하는 방법을 제공한다.**

**물리적 세계에서는 모든 물체가 구별된다. 자세히 보면 두 개의 눈송이도 다르다. 따라서 두 개의 물리적 물체는 서로 정확하게 `동등(equal)` 하지 않다. → 유사성 정도만 가지고 있음**

그러나 인간 언어의 세계와 수학적 개념에서는 동일한 것에 대해 이름을 가질 수 있다.

---

## ****Three Ways to Regard Equality(동일성을 다루는 3가지)****

공식적으로 우리는 동일한 것을 여러 가지 방식으로 간주할 수 있다.

- **Using an abstraction function** :  추상화 함수 f : R → A는 데이터 유형의 구체적인 인스턴스를 해당 Abstract value에 매핑한다는 점을 기억해라. f를 동등성에 대한 정의로 사용하려면, f(a) = f(b)인 경우에만 a가 b와 같다고 말할 수 있다.

- **Using a relation** : Equality는 E ⊆ T x T 이다
    - **reflexive(재귀적):** E(t,t) ∀ t ∈ T
    - **symmetric(대칭적):** E(t,u) ⇒ E(u,t)
    - **transitive(이행적):** E(t,u) ∧ E(u,v) ⇒ E(t,v)

E를 Equality에 대한 정의로 사용하려면 E(a,b)인 경우에만 a가 b와 같다고 말할 수 있다.

이 두 가지 개념은 동일하다. 동등 관계는 추상화 함수를 유도한다.(관계는 T를 분할하므로 f는 각요소를 해당 분할 클래스에 매핑한다.) 추상화 함수에 의해 유도된 관계는 등가 관계이다.

abstract value 사이의 평등에 대해 이야기할 수 있는 세 번째 방법은 클라이언트가 이에 대해 관찰할 수 있는 측면이다.

- **Using observation** : 관찰로 구별할 수 없는 두 오브젝트는 동일하다고 말할 수 있다. 적용할 수 있는 모든 작업은 두 오브젝트에 대해 동일한 결과를 생성한다. 집합 표현식 {1, 2} 과 {2, 1}를 고려해라.
    - |{1,2}| = 2 및 |{2,1}| = 2
    - 1 ∈ {1,2}는 참이고, 1 ∈ {2,1}은 참
    - 2 ∈ {1,2}는 참이고, 2 ∈ {2,1}은 참입니다.
    - 3 ∈ {1,2}는 거짓이고, 3 ∈ {2,1}은 거짓입니다.
    - … 등등

Abstract data type의 측면에서 “Observation”은 객체에 대한 작업 호출을 의미한다. 따라서 두 객체는 Abstract data type의 operation을 호출하여 구별할 수 없는 경우에만 동일하다.

### Example: Duration(기간)

다음은 immutable ADT의 간단한 예시이다.

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

**이제 다음 중 어느 값이 같은 것으로 간주되어야 할까?**

```java
Duration d1 = new Duration (1, 2);
Duration d2 = new Duration (1, 3);
Duration d3 = new Duration (0, 62);
Duration d4 = new Duration (1, 2);
```

**Equality에 대한 abstraction-function 정의와 observational equality 정의의 관점에 서 생각해보아라**

---

## == vs. equals()

**많은 언어와 마찬가지로 Java에는 서로 다른 의미 체계를 사용하여 동일성을 테스트하기 위한 두 가지 다른 작업이 있다.**

- **연산자 `==` 는 참조 값을 비교한다. 보다 정확하게는 `referentila equality` 를 비교한다. 두 참조가 메모리의 동일한 저장소를 가리키는 경우 ==가 true이다. 우리가 그린 스냅샷 다이어그램을 예시로 볼 때 `==` 화살표가 동일한 객체는 가르키는 두 개의 참조가 있다.**
- `**equals()` operation은 객체 내용을 비교한다. 즉, 이 문서에서 이야기한 의미에서 object equality를 비교한다. → equals operation은 모든 Abstract data type에 대해 적절하게 정의되어야 한다.**

**비교를 위해서 다음은 여러 언어의 비교 연산자를 보겠다.**

|  | referential equality | object equality |
| --- | --- | --- |
| Java | == | equals() |
| Objective C | == | isEqual: |
| C# | == | Equals() |
| Python | is | == |
| Javascript | == | n/a |

**Java와 Python 사이에서 `==` 의미가 바뀌는 점에 유의해라. Java에서 `==` 는 참조값만 테스트할 뿐 객체 내용을 비교하지 않는다.**

**이러한 언어를 사용하는 프로그래머로서 우리는 참조 비교 연산자의 의미를 변경할 수 없다.**

**Java에서 `==` 는 항상 참조를 비교한다. 그러나 새로운 데이터 타입을 정의할 때 해당 데이터 유형의 값에 대한 객체 Equality가 무엇을 의미하는지 결정하고 `equals()` operation을 적절하게 구현하는 것은 우리의 책임이다.**

---

## ****Equality of Immutable Types****

`Object`에 의해서 `equals()`는 정의되며, 구현은 다음과 같다.

```java
public class Object {
    ...
    public boolean equals(Object that) {
        return this == that;
    }
}
```

즉, `euqlas()`의 기본 의미는 참조 비교와 동일하다. immutable data type의 경우 항상 잘못된 경우일 것이다. 따라서 `euqlas()` 메서드를 overriding(재정의)하여 구현을 바꿔야 한다.

`Duration` 의 첫번째 변경 시도

```java
public class Duration {
    ...   
    // Problematic definition of equals()
    public boolean equals(Duration that) {
        return this.getLength() == that.getLength();        
    }
}
```

이 코드는 미묘하게 문제가 있다. 다음 코드를 시도하면 작동이 안됨

```java
Duration d1 = new Duration (1, 2);
Duration d2 = new Duration (1, 2);
Object o2 = d2;
d1.equals(d2) → true
d1.equals(o2) → false
```

이 코드가 실제로 동작할 때 `d2` 와 `o2`가 메모리에서 동일한 객체를 참조하더라도 `equals()` 는 여전히 다른 결과를 얻는다는 것을 알 수 있다.

메소드 시그니처가 `Object`의 시그니처와 동일하지 않기 때문에  `Duration` 이 `equals()`를 오버라이딩한 것이 아닌 오버로딩한 것으로 나타난다.

실제로 `Duration` 에는 두가지 `equals()` 가 있다. Object에서 상속된 `equals(Object)`와 새로운 `equals(Duration)` 가 있다.

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

우리는 static checking에서 부터 overloading를 보았다. 컴파일러가 파라미터의 컴파일 타임에 유형을 사용해서 오버로드된 작업 중에 선택하는 것을 기억해라.

예를 들어 `/` 연산자를 사용한다면 컴파일러는 파라미터가 int인지 float인지에 따라 정수 나누기 혹은 실수 나누기를 선택한다. 여기서도 동일한 컴파일 타임 바인딩이 발생한다.

- `d1.equlas(o2)` 에서와 같이 `Object`참조를 전달하면 결국 `equals(Object)` 를 호출하게 된다.
- `d1.equals(d2)` 에서와 같이 `Duration` 참조를 전달하면 결국 `equals(Duration)`를 호출하게 된다.

→ 이는 런타임에 `o2` 와 `d2` 가 모두 동일한 객체를 가르키더라도 발생한다.(Equality에 일관성이 사라졌다.)

메서드 시그니처에 실수를 하고 메서드를 오버라이딩하려고 했을 때 메서드가 오버로딩되기 쉽다. Java에서 언어 기능인 `@override` 애노테이션이 있기 때문에 발생하는 일반적인 오류이다.

→ 이 주석은 슈퍼 클래스의 메소드를 오버라이딩 할 때 마다 사용해주어야 한다.

`@Override` 를 사용하면 Java 컴파일러는 동일한 시그니처를 가진 메서드가 실제로 슈퍼클래스에 존재하는지 확인하고 시그니처에 실수가 있는 경우에 컴파일러 오류를 표시한다.

따라서 `Duration` 의 `equals()` 를 구현하는 올바른 방법은 다음과 같다.

```java
@Override
public boolean equals (Object thatObject) {
    if (!(thatObject instanceof Duration)) return false;
    Duration thatDuration = (Duration) thatObject;
    return this.getLength() == thatDuration.getLength();
}
```

이렇게 하면 문제가 해결된다.

```java
Duration d1 = new Duration(1, 2);
Duration d2 = new Duration(1, 2);
Object o2 = d2;
d1.equals(d2) → true
d1.equals(o2) → true
```

### Instanceof

- `Instanceof` 연산자는 객체가 해당 유형의 인스턴스인지 여부를 테스트한다.
- 우리가 선호하는 static checking이 아닌 dynamic type checking이다.
- `instanceof` 는 `equals` 를 제외하고는 어느 곳에서도 허용되지 않는다.

→ 이러한 규제 사항은 `getClass` 와 같은 객체의 런타임 유형을 검사하는 방법도 포함된다.

---

## The Object Contract

- `Object` 클래스의 Spec은 매우 중요하며 종종 `Object`계약서라고도 불린다.
- 계약은 Object클래스의 메서드 스펙에서 확인할 수 있다.(여기서는 `equals`계약에 중점)
- `equals` 메서드를 오버라이딩하는 경우 계약을 지켜야한다.
    - `equals` 는 동등관계를 정의해야한다. : 즉 대칭적, 이행적 관계를 정의해야한다.
    - `**equals` 는 일관성이 있어야한다. : 개체에 비교에 사용된 정보들이 수정되지 않은 경우 `equals`**를 반복 호출해도 동일한 결과를 생성해야 한다.
    - null이 아닌 참조 값 `x` 의 경우 x.equals(null)은 `false` 를 리턴한다.
    - `equals` 메서드에 의해 동일하다고 간주되는 두 개체에 대해서 `hasCode` 는 동일한 결과를 생성해야 한다.

### Breaking the Equivalence Relation

우리는 `equals` 에 의해 구현된 동등의 정의가 실제로 앞서 정의된 Equivalence Relation(반사적, 대칭적, 이행적)인지 확인해야 한다. 그렇지 않은 경우 Equality에 의존하는 operation(ex. sets, searching)은 불규칙하고 예측할 수 없게 작동한다. 

→ 우리는 a와 b는 같지만 b와 a는 다른 코드를 작성하고 싶지 않다. 힘듬

equality를 더욱 유연하게 만들려는 시도가 잘못될 수 있는지 보여주는 예시이다.

→ `Duration` 개체를 비교할 때 서로 다른 컴퓨터에는 시계가 동기화되지 않을 수 있으므로 허용 오차를 설정한다고 가정하자.

```java
private static final int CLOCK_SKEW = 5; // seconds

@Override
public boolean equals (Object thatObject) {
    if (!(thatObject instanceof Duration)) return false;
    Duration thatDuration = (Duration) thatObject;
    return Math.abs(this.getLength() - thatDuration.getLength()) <= CLOCK_SKEW;
}
```

**→ Equivalence relation의 어떤 속성이 위반될까?**

### Breaking Hash Tables

`hashCode` 메서드와 관련된 계약부분을 이해하려면 해시 테이블에 작동 방식에 대한 몇가지 개념이 필요하다.

- 매우 일반적인 컬렉션 구현인 `HastSet` 와 `HashMap` 는 해시 테이블 데이터 구조를 사용하고 `hashCode` 메서드에 의존하여 세트에 저장되고 맵에서 키로 사용되는 객체에 올바르게 구현된다.

해시 테이블은 매핑을 표현한 것이다. 즉, 키를 값에 매핑하는 Abstract data type이다. 해시 테이블은 지속적인 시간 조회를 제공하므로 트리나 목록보다 성능이 더 좋은 경향이 있다. 키는 정렬될 필요가 없으며 `equals` 및 `hashCode` 를 제외하고는 특정한 속성을 가질 필요가 없다.

해시 테이블의 작동 방식은 다음과 같다. 여기에 삽입될 것으로 예상되는 요소 수에 해당하는 크기로 초기화된 배열이 포함되어 있다. 삽입할 키와 값이 제시되면 키의 해시코드를 계산하고 이를 배열 범위의 인덱스로 변환한다.(ex. 모듈로 나누기), 그런 다음 해당 인덱스에 값이 삽인된다.

해시 테이블의 rep invariant는 키가 해시 코드에 의해 결정된 인덱스에 있다는 기본 제약 조건을 포함한다.

해시코드는 키가 인덱스 전체에 고르게 분산되도록 설계되었다. 그러나 때때로 충돌이 발생하여 두 개의 키가 동일한 인덱스에 배치된다. 따라서 해시 테이블은 인덱스에 단일 값을 보유하는 대신 일반적으로 `hash bucket` 이라고 하는 키/값 쌍 목록을 실제로 보유한다. 키 / 값 쌍은 Java에서 단순히 두 개의 필드가 있는 클래스로 구현된다. 삽입 시 해시 코드에 의해 결정된 배열 목록에 쌍을 추가한다. 조회를 위해 키를 해싱하고 올바른 값을 찾는 다음 키가 쿼리 키와 동일한 것을 찾을 때 까지 각 쌍을 검사한다.

이제 계약에서 동일한`Object`객체가 동일한 해시코드를 갖도록 요구하는 이유가 분명해졌다. 두 개의 동일한 객체에서 서로 다른 해시코드가 있는 경우 서로 다른 배열에 배치될 수 있다. 따라서 삽입된 키와 동일한 키를 사용하여 값을 조회하려고 하면 조회가 실패할 수 있다.

`Ojbect`의 기본 `hashCode()`  구현은 `equals()` 와 일치한다.

```java
public class Object {
  ...
  public boolean equals(Object that) { return this == that; }
  public int hashCode() { return /* the memory address of this */; }
}
```

참조 `a` 와 `b` 가 동일하다고 가정하면 a의 주소와 b의 주소는 동일하다. 그래서 `Object` 의 계약을 만족한다.

그러나 immutable object에는 `hashCode` 의 다른 구현이 필요하다.

`Duration` 은 아직 `hashCode()` 를 오버라이딩 하고 있지 않으므로 `Object` 계약을 위반하고 있다. 

```java
Duration d1 = new Duration(1, 2);
Duration d2 = new Duration(1, 2);
d1.equals(d2) → true
d1.hashCode() → 2392
d2.hashCode() → 4823
```

`d1` 과 `d2` 는 같다. 하지만 hash code는 다르다. 그래서 우리는 고쳐야한다.

계약이 충족되었는지 확인하는 간단한 방법은 항상 `hashCode()`가 상수 값을 반환하도록 하는 것이다. → 따라서 모든 개체의 해시 코드가 동일하도록 하게 됨

이는 `obejct` 계약을 충족하지만 모든 키가 동일한 슬롯(동일한 해시 테이블)에 저장되고 모든 조회가 긴 목록을 따라 선형 검색으로 변질되므로 성능에 좋지 않은 영향을 미친다.

`hashCOde` 의 계약을 충족하는 더 합리적인 해시 코드를 구성하는 표준 방법은 Euqality 결정에 사용되는 객체의 각 구성 요소에 대한 해시 코드를 계산(각 구성의 요소의 메서드를 호출해서)하고 이를 결합하는 것이다. 몇가지 산술 연산을 수행한다. `Duration` 의 경우 클래스 의 추상 값이 이미 정수 값이므로 쉽다.

```java
@Override
public int hashCode() {
    return (int) getLength();
}
```

동일한 객체가 동일한 해시 코드 값을 가져야 한다는 요구 사항을 충족하는 한 사용하는 특정 해싱 기술은 코드의 정확성에 영향을 주지 않는다. 

→ 서로 다른 객체간 불필요한 충돌을 만들어 성능에 영향을 미칠 수 있지만 성능이 좋지 않은 해시 함수라도 계약을 위반하는 해시 함수보다 낫다.

가장 중요한 것은 전혀 `hashCode` 를 전혀 재정의 하지 않는다면  객체의 주소를 기반으로 하는 `Object`에서 해시코드를 얻게된다는 점이다.

만약 `equals` 를 재정의 했다면 이는 계약을 확실히 위반했다는 것을 의미한다. 그래서 일반적으로 

> `equals`를 오버라이딩한다면 `hashCode` 도 항상 오버라이딩 해야한다.
> 

---

## ****Equality of Mutable Types****

**우리는 지금까지 이 글을 읽으면서 immutable object의 Equality에 초점을 맞추왔다.**

**mutable object는 어떨까?**

**우리의 정의를 기억해 보아라. Observation로 구별할 수 없는 두 객체는 동일하다. 변경 가능한 객체의 경우 이를 해석하는 방법은 두가지가 있다.**

- **객체의 상태를 변경하지 않는 Observation, 즉 `observer`, `producer` 그리고 `creator` 메서드만 호출하여 구별할 수 없는 경우, 이는 프로그램의 현재 상태에서 두 개체가 동일하게 보이는지 여부를 테스트하기 때문에 엄격하게 `Observational equality` 라고 부르는 경우가 많다.**
- **어떤 관찰로도 구별할 수 없으면 상태도 변한다. 이 해석을 통해 mutator를 포함하여 두 객체에 대한 모든 메서드를 호출할 수 있다. 이는 두 개체가 현재 상태와 미래의 모든 상태에서 동일하게 “동작”하는지 여부를 테스트하기 때문에 종종 `behavioral equality` 라고 한다.**

**immutable object의 경우, mutator 메서드가 없기 때문에 Observational 및 behavioral의 equality는 동일하다.**

**변경 가능한 객체의 경우 엄격한 Observational eqaulity를 구현하고 싶은 유혹이 있다. 실제로 Java는 대부분 mutable data type에 대해서 observational equality를 사용한다.**

만약 서로 다른 `List` 객체에 동일한 요소가 포함된다면 `equals()` 는 두 객체가 동일하다고 보면 된다.

그러나, observational equality를 사용하면 미묘한 버그가 발생하고 다른 컬렉션 데이터 구조의 rep invariant를 어길 수 있다. 

`List` 를 만든 뒤 `Set` 에 넣는다고 가정해보자.

```java
List<String> list = new ArrayList<>();
list.add("a");

Set<List<String>> set = new HashSet<List<String>>();
set.add(list);
```

우리는 Set에 우리가 넣은 List가 포함되어 있는지 확인할 수 있으며, 실제로는 다음과 같다.

```java
set.contains(list) → true
```

하지만 이제 List를 변경한다.

```java
list.add("goodbye");
```

그 후 더 이상 Set에 나타나지 않는다.

```java
set.contains(list) → false!
```

사실 그보다 더 안좋은 것은  집합의 멤버를 반복할 때 여전히 거기에 있는 List를 찾았지만 `contatins()` 에서는 List가 없다고 하는 것이다.

```java
for (List<String> l : set) { 
    set.contains(l) → false! 
}
```

`Set`자체에 iterator과 자체 `contatins()` 메소드가 원소가 집합에 있는지 여부에 대해 동일하지 동의하지 않으면 확실히 set은 깨진다.

왜 그럴까? `List<String>` 는 mutable object 이다. `List` 와 같은 표준 자바 구현에서 mutation은 `equals()` 그리고 `hashCode()` 에 영향을 미친다. 

List가 처음 `HashSet` 에 포함될 때 해당 시점에 `hashCode()` 에 해당하는 hash bucket에 저장된다. 

→ 이 후 `List` 가 변경된다면 `HashSet` 은 List의 `HashCode()`가 변경되어도 다른 버킷으로 이동되어야 한다는 것을 인식하지 못한다.

`equals()` 와 `hashCode()`가 mutable의 영향을 받을 수 있는 경우 해당 객체를 키로 사용하는 해시 테이블의 `rep invariant`를 깨드릴 수 있다.

다음은 `java.util.Set` 에 대한 스펙이다.

> Note: Great care must be exercised if mutable objects are used as set elements. The behavior of a set is not specified if the value of an object is changed in a manner that affects equals comparisons while the object is an element in the set.
(참고: 변경 가능한 객체가 원소로 사용되는 경우 세심한 주의가 필요하다. 객체가 집합의 요소인 동안 객체의 값이 변경되면 Set의 operation은 지정되지 않는다.)
> 

불행히도 Java 라이브러리는 mutable class에 대한 `equals()` 에 대한 해석에 일관성이 없다.

컬렉션은 observational equality를 사용하지만 다른 mutable class(ex. `StringBuilder`)는 behavioral equality를 사용한다.

우리가 알아야 할 정보는 `behavioral equality` 를 구현해야 한다는 것이다. 일반적으로 이는 두 참조가 동일한 객체에 대한 alias인 경우에만 `equals()` 함을 의미한다. 

→ 따라서 mutable object는 `Object` 는 `eqauls()` 와 `hashCode()` 를 상속받아야 한다.

observational equality(두 개의 변경 가능한 객체가 현재 상태에서 동일하게 보이는지 여부)이 필요한 클라이언트의 경우 새로운 메서드(ex. `similar()` )를 정의하는 것이 좋다. 

---

## ****The Final Rule for equals() and hashCode()****

**immutable type의 경우:**

- `equals()` 는 abstract values를 비교해야한다. 이는 `equals()` 가 behavioral equality를 제공해야한다고 말하는 것과 같다.
- `hashCode()` 는 abstract value를 정수로 매핑해야한다.

따라서 immutable type는 `equals()`및 `hashCode()` 를 모두 오버라이딩해야한다.

**mutable type의 경우:**

- `equals()` 는 `==` 처럼 참조를 비교해야한다. 다시 말하지만 `equals`는  behavioral eqaulity를 제공해야한다.
- `hashCode()` 는 참조를 정수로 매핑해야한다.

따라서 mutable type은 `equals()` 및 `hashCode()` 를 오버라이딩해서는 안되며 `Obejct`가 제공하는 기본 구현을 사용해야 한다.  

Java는 컬렉션에 대해 이 규칙을 따르지 않는다. → 이게 함정

### ****Autoboxing and Equality****

Java의 또 다른 함정. 우리는 primitive trpe과 wrapper type에 대해서 이야기 했다.

ex) `int` , `Integer` 

객체 유형은 올바른 방식으로 `equlas()`를 구현하므로  동일한 값을 가진 `Integer` 객체를 구현하면 `equals()`는  true가 된다.

```java
Integer x = new Integer(3);
Integer y = new Integer(3);
x.equals(y) → true
```

하지만 여기에는 미묘한 문제가 있다. `--` 는 오버라이딩 상태이다. 

예를 들어 `Integer` 와 같은 경우 referential equality를 구현한다.

```java
x == y // returns false
```

그러나 `int` 와 같은 primitive의 경우 `==` 즉, behavioral equality를 구현한다.

```java
(int)x == (int)y // returns true
```

따라서 `Integer` 와 `int` 를 서로 바꿔서 사용할 수는 없다. Java가 자동으로 `Integer`과 `int` 사이를 변환한다는 사실(autoboxing  및 autounboxing 라고 함)로 인해서 미묘하게 버그가 발생할 수 있다. 이를 고려해야한다.

```java
Map<String, Integer> a = new HashMap(), b = new HashMap();
a.put("c", 130); // put ints into the map
b.put("c", 130);
a.get("c") == b.get("c") → ?? // what do we get out of the map?
```

---

## ****Summary****

- Equality는 동등 관계(반사적, 대칭적, 이행적)이여야 한다.
- equals와 hashCode는 서로 일치해야 해시 테이블(ex. `HashSet` 및 `HashMap` )을 사용하는 데이터 구조가 제대로 작동한다.
- Abstraction function는 immutable data type의 동일성을 위한 기초이다.
- 참조 동일성은 mutable data type의 equality의 기초이다. 이는 시간이 지남에 따라서 일관성을 보장하고 해시 테이블의 대표 불변성을 깨드리는 것을 방지할 수 있는 유일한 방법이다.

equality는 Abstract data type구현의 한 부분이며, 우리는 세 가지 목표를 달성하는데 ADT가 얼마나 중요한지 확인했다. 

- **SFB** :  Set 및 Map과 같은 컬렉션 데이터 유형과 함께 사용하려면 equality 및 hashCode의 올바른 구현이 필요하다. 또한 테스트 작성에도 매우 바람직하다. 자바의 모든 객체는 `Object` 구현을 상속하므로 immutable type은 이를 오버라이딩 해야한다.
- **ETU** : 우리는 스펙을 읽는 클라이언트와 다른 프로그래머는 우리 유형이 적절한 equals 연산을 구현하기를 기대할 것이다.
- **RFC** : immutable type에 대해 올바르게 구현된 equality는 참조의 동일성과 abstract value의 동일성을 분리하여 값 공유 여부에 대한 결정을 클라이언트에게 숨긴다. mutable type에 대해 observational equality 보다는 behavioral equality을 선택하면 예상치 못한 alias 버그를 방지하는 데 도움이 된다.