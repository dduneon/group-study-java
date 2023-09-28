# Mutability & Immutability

Objectives

- `**mutability` 와 `mutable` 인 object 이해하기**
- `**aliasing` 를 식별하고 `mutability` 의 위험을 이해한다.**
- `**immutability`를 사용하여 정확성과 명확성 및 변경 가능성을 향상한다.**

---

## Mutability

`**snapshot diagrams` 에 대해 이야기 했을 때 한번 생성하면 항상 같은 값을 같는  `immutable` 객체가 있었다.**

**그 외에는 오브젝트의 값을 변경 할 수 있는 메서드가 있는 `mutable` 객체이다.**

`**String` 는 `immutable type` 이다. → 항상 같은 값을 가진다.**

`**StringBuilder` 은 `mutable type` 이다. → 문자열의 일부를 삭제하거나 삽입 대체 할 수 있다.**

![http://web.mit.edu/6.005/www/fa15/classes/09-immutability/figures/reassignment.png](http://web.mit.edu/6.005/www/fa15/classes/09-immutability/figures/reassignment.png)

`**String`는 불변이므로 한 번 생성되면 `String`는 항상 동일한 값을 갖는다.** 

**만약 끝에 글자를 추가하기 위해서는 새로운 String obejct를 만들어야한다.**

```java
String s = "a";
s = s.concat("b"); // s+="b" and s=s+"b" also mean the same thing
```

![http://web.mit.edu/6.005/www/fa15/classes/09-immutability/figures/mutation.png](http://web.mit.edu/6.005/www/fa15/classes/09-immutability/figures/mutation.png)

**이와 반대로 `StringBuilder` 은 `mutable` 하다. 이것은 단순히 새로운 값을 반환하는 것이 아니라 객체의 값을 변경하는 메서드가 있다.**

```java
StringBuilder sb = new StringBuilder("a");
sb.append("b");
```

또한 문자열의 일부를 삭제하거나 개별 문자열을 삽입, 변경하는 다른 메서드가 존재한다.

**결론은 둘 다 `“ab”` 라는 값을 참조한다.** 

- **객체에 대한 참조가 하나만 있는 경우에는 `mutability` 와 `immutability` 의 차이는 별로 중요하지 않다.**
- **객체에 대한 다른 참조가 있을 때 동작 방식은 차이가 생긴다.**

예를 들어, 변수 `t`가 `s`와 동일한 `String`객체를 가르키고 다른 변수 `tb`가 `sb` 와 같은 `StringBuilder`객체를 가르킬 때 차이가 분명해진다.

![http://web.mit.edu/6.005/www/fa15/classes/09-immutability/figures/string-vs-stringbuilder.png](http://web.mit.edu/6.005/www/fa15/classes/09-immutability/figures/string-vs-stringbuilder.png)

```java
String t = s;
t = t + "c";

StringBuilder tb = sb;
tb.append("c");
```

프로그래밍에서는 왜 `StringBuilder` 을 사용하는 일반적인 이유는 수많은 문자열을 연결 할 때 사용된다.

```java
String s = "";
// char[] sc = new char[1];
for (int i = 0; i < n; ++i) {
			/*
			char[] tmp = new char[sc.length + 1];
			for(int j = 0; j < tmp.length; j++){
				tmp[j] = sc[j];
			}
			tmp[i] = n - '0';
			sc = tmp
			*/
			s = s + n;
}

```

`immutable string` 을 사용하면 이것은 임시 복사본을 많이 만든다.

1. **문자열의 첫번째 숫자인 “n”은 실제로 마지막 문자열을 구성하는 과정에서 n번 복사된다.**
2. **두번째 숫자는 n-1번 복사된다.**
3. **n개 문자들을 연결하는 과정이지만 그 사이에 복사를 하는 과정 때문에 실제로는 O(n^2)이라는 시간 복잡도가 생성된다.**

`StrringBuilder` 은 이러한 복사를 최소화하도록 설계되었다. 간단하지만 똑똑한 내부 데이터 구조를 이용한 `toString()` 를 호출하여 최종 문자열을 요청할 때 끝까지 복사를 수행하지 않는다.

```java
StringBuilder sb = new StringBuilder();
for (int i = 0; i < n; ++i) {
  sb.append(String.valueOf(n));
}
String s = sb.toString();
```

**성능이 좋은 것도 우리가 `mutable object` 를 사용하는 이유 중 하나이고 또 다른 이유는 편리한 공유이다.** 

**프로그램의 두 부분이 공통적으로 변경 가능한 데이터 구조를 이용하여 편리하게 통신 할 수 있다.**

---

## Risks of mutation

`**Mutable type` 은 `immutable types` 보다 훨씬 좋아보인다.** 

`**StringBuilder`은 `String` 역할 외에 `set()` , `append()` 등 메서드를 사용 할 수 있다.**

**`immutable types`은 왜 쓰는걸까?**

**→ SFB, ETU, RFC**

`**Mutability` 는 프로그램이 하는 것을 더 이해하기 힘들게 만들고 계약을 시행하기 더 어려워진다.**

두가지 예시

### Risky example #1: passing mutable values

**List 정수를 합하여 리턴하는 간단한 예시 이다.**

```java
/** @return the sum of the numbers in the list */
public static int sum(List<Integer> list) {
    int sum = 0;
    for (int x : list)
        sum += x;
    return sum;
}
```

절대값으로 합하는 메서드도 필요하다고 가정하자. 

**우리는 DRY(Don’t Repeat Yourself)원칙을 따라서 위에서 만들어놓은 `sum()` 메서드를 사용해서 구현한다.**

```java
/** @return the sum of the absolute values of the numbers in the list */
public static int sumAbsolute(List<Integer> list) {
    // let's reuse sum(), because DRY, so first we take absolute values
    for (int i = 0; i < list.size(); ++i)
        list.set(i, Math.abs(list.get(i)));
    return sum(list);
}
```

**이 메서드에서는 List의 값을 직접 변경하여 작업을 수행한다.** 

- 기존 List를 재사용하는 것이 더 효율적이라고 생각했기때문에 구현자는 그렇게 구현했다.
- List가 만약 수만개의 항목을 가졌다면은 그만큼 절대값 메모리를 생성하는데 드는 시간과 메모리를 절약한다.
- 그러므로 구현자는 DRY원칙과 성능이라는 두가지를 얻었다.

**그러나 결과는 예상과 다르게 된다.**

```java
// meanwhile, somewhere else in the code...
public static void main(String[] args) {
    // ...
    List<Integer> myData = Arrays.asList(-5, -3, -2);
    System.out.println(sumAbsolute(myData)); // expected: 10 actual: 10
    System.out.println(sum(myData)); // expected: -10 actual: 10
}
```

- **Safe form bugs? :** 예제에서는 `subAbsolute()` 는 스펙이 허용하는 수준을 넘었기때문에 비난 당할 수 있다. 실제로 변경 가능한 객체를 전달하는 것은 숨어있는 버그이다. 단지 프로그래머가 List을 의도치 않게 바꿔지기를 기다리는 것이며, DRY와 재사용과 같은 매우 좋은 의도를 가지고 있어도, 결과적으로 매우 추적하기 어려울 수 있다.
- **Easy to undetstand? :** `main()` 을 읽을 때 `sum()` 과 `sumAbsolute()` 는 어떻게 추정 할 수 있는가? `myData` 가 어디서 변경되는지 알 수 있을까?

### Risky example #2: returning mutable values

**우리는 변경 가능한 객체를 메서드 파라미터로 넘기면 문제를 야기한다는 것을 확인했다.**

**그러면 mutable obejct를 리턴하면 어떨까?**

**자바 내장 함수인 `Date` 를 생각해보자. `Data` 는 mutable type이다.**

**봄의 첫날을 결정하는 메서드가 있다고 가정할 때**

```java
/** @return the first day of spring this year */
public static Date startOfSpring() {
    return askGroundhog();
}
```

**봄이 시작되는 시기를 계산하기 위해서 잘 알려진 Groundhog algorithm 알고리즘을 사용하고 있다.**

 Groundhog algorithm : 그라운드 호그 꺼내서 자기 그림자 보면 봄이 늦게오고  보지 않으면 일찍 옴

**파티를 계획 할 때 이러한 메서드를 사용한다.**

```java
// somewhere else in the code...
public static void partyPlanning() {
    Date partyDate = startOfSpring();
    // ...
}
```

모든 코드가 작동한다. 독립적으로는 두가지 일이 발생한다. **첫째로는 `startOfStrping()` 메서드에서 봄이 언제 시작 될 것인지 끊임없이 물어보는 것에 대해 짜증이 난다는 것이다.** 

그래서 코드는 한번만 질문하고 답변을 캐시하여 호출마다 반환하도록 한다.

```java
/** @return the first day of spring this year */
public static Date startOfSpring() {
    if (groundhogAnswer == null) groundhogAnswer = askGroundhog();
    return groundhogAnswer;
}
private static Date groundhogAnswer = null;
```

**private static은 전역변수로 간주 할까 아닐까?**

→ startOfSpring() 메서드에서만 사용하기 위해서 선언한 것이므로 전역적이지 않다.

두번째로는 `startOfSpring()` 클라이언트 중 한명이 봄의 첫 날은 파티에 너무 춥다고 결정하기 때문에 파티는 정확히 한 달 후에 열릴 것이다.

```java
// somewhere else in the code...
public static void partyPlanning() {
    // let's have a party one month after spring starts!
    Date partyDate = startOfSpring();
    partyDate.setMonth(partyDate.getMonth() + 1);
    // ... uh-oh. what just happened?
}
```

**이 코드에서 한 달을 추가하는 방식에도 잠재적인 버그가 있다. 봄이 시작 될 때를 암시적으로 가정하는 것은?**

1. `partyPlanning()` 와 `groundhogAnswer` 이 동일한 `Date`객체를 가르키고 있었다.
2. 12월일 땐 index가 11이므로 1을 더해주면 12가 돼서 잘못된 값이 설정된다.

**이 두가지 결정이 상호작용 하면 어떻게 될까? 이 버그를 어디서 처음 발견 할 수 있을까?** 

`startOfSpring()` , `partyPlanning()` , 또는 `startOfSpring()` 를 호출하는 곳?

**Key point**

- **Sage from bugs :** 우리는 이번에도 버그를 가지고 있었음
- **Ready for change** : 날짜 객체는 변경이 가능하지만 여기서 말하는 변화는 그런 변화가 아니다. 우리는 프로그램의 코드를 다시 쓰거나 버그가 생기지 않게 프로그램을 리팩토링 할 수 있는지가 여부였다. 이 예제에서의 두가지 변화는 전부 버그가 발생했다.

**위 예제에서 `List<Integer>`과 `Date` 가 Immutable type상태라면 설계상 버그는 아예 존재하지 않았을 것이다.**

실제로 `Date` 는 사용하지 않고 `java.time.LocalDateTime` , `Instant` 등을 사용한다. 

→ `immutable` 를 보장해주는 스펙을 가졌음

**또 한 예시들은 mutable 객체를 사용하는것이 실제로 성능에는 좋지 않을 수 있다는 것을 보여줬다.**

**이 버그에 대한 스펙이나 메서드 시그니처의 변경을 피하는 가장 간단한 해결책으로는 반환 할 때 복사본을 반환하는 것이다.**

```java
return new Date(groundhogAnswer.getTime());
```

**→ 이러한 패턴은 `defensive copying` 라고한다.**

- `**defensive copying` 는 `startOfSpring()` 에서 저장된 날짜에게 영향을 주지 않고 반환된 날짜를 자유롭게 변경 할 수 있는 것을 의미한다.**
- **그러나 이러한 방법을 사용하면 `startOfSpring()` 에서 모든 클라이언트에 대해 추가 작업을 수행하고 추가 공간을 사용한다.(클라이언트 99%가 날짜를 변경하지 않더라도)**
- **그래서 이런 방법을 사용하면 봄의 첫날에 대한 복사본을 아주 많이 가질수도 있음**
- **대신 `immutable type`을 사용하게 된다면 같은 값을 안전하게 공유 할 수 있으므로 복사 및 메모리 공간이 덜 필요하다.**

**→ `immutability` 는 `defensive copying` 를 안하기 때문에 `mutability` 보다 효과적일 수 있다.**

---

## ****Aliasing is what makes mutable types risky****

`**mutable object`는 메서드내에 로컬로 사용하고 객체에 대한 참조가 하나만 있는 경우면 사용하기 좋다.**

**위 예시에서 나온 두가지 예시의 원인은 같은 mutable object를 참조하는  여러가지 참조가 있다는 것이다.** 

→ **aliases(별칭)이라고도 함**

**스냅샷 다이어그램을 통해 예제를 보면 알 수 있음**

- **위 `List` 예시에서, sum과 sumAbsolute 메서드에서 사용하는 `List` 는 main에서 사용하는 myData를 가르킨다. `sumAbsolute`를 만든 프로그래머는 `list` 가 수정해도 된다고 생각한다. `main`  프로그래머는 형태가 유지되길 원한다.**

→ aliases 때문에 프로그래머는 리스트 값을 잃었다.

- **위 `Data` 예시에서, `Data` 오브젝트를 가르키는 변수 이름은 `groundhogAnswer` 과 `partyDate` 가 있다. 이러한 aliases(별칭)은 코드의 완전히 다른 부분에 있으며 다른 프로그래머가 무엇을 하는지 전혀 모르는 상태로 제어를 받는다.**

---

## Specifications for mutating methods

메서드가 mutation을 수행할 때 메서드 스펙에 해당 mutation을 포함하는 것이 중요하다는 것을 알게 됐다.

**→ 우리는 이제 특정 메서드가 객체의 상태를 변경하지 않아도 해당 객체의 변경 가능성이 여전히 버그 발생 가능성이 있다는 것을 알았다.**

**mutating method 예시**

```java
static void sort(List<String> lst)
  requires: nothing
  effects:  puts lst in sorted order, i.e. lst[i] <= lst[j]
              for all 0 <= i < j < lst.size()
```

**not mutate argument method 예시**

```java
static List<String> toLowerCase(List<String> lst)
  requires: nothing
  effects:  returns a new list t where t[i] = lst[i].toLowerCase()
```

`**effects` 에서 입력값이 변할 수 있다고 말하지 않으면 6.005에서는 입력값을 변경하는 것을 암시적으로 허용하지 않는다고 한다.**

→ 사실상 모든 프로그래머들은 갑작스러운 변형으로인해 끔찍한 버그가 발생할 수 있다고 생각한다.

---

## Iterating over arrays and lists

**살펴볼 객체는 `iterator` 객체이다. 컬렉션을 한개씩 반환하는 객체이다.**

`**iterator` 은 List나 배열을 단계별로 실행 할 때 for루프에서 자바 자체적으로 사용한다.**

```java
List<String> lst = ...;
for (String str : lst) {
    System.out.println(str);
}
```

→ 컴파일러에 의해서 다시 작성된다.

```java
List<String> lst = ...;
Iterator iter = lst.iterator();
while (iter.hasNext()) {
    String str = iter.next();
    System.out.println(str);
}
```

`**iterator` 두가지 메소드가 있다**

- `**next()` : 컬렉션의 다음요소를 반환한다.**
- `**hasNext()` : iterator이 컬렉션 끝에 도달했는지 확인 가능하다.**

**next() 메서드는 요소를 반환할 뿐만 아니라 후속 호출이 다른 요소를 반환하도록 iterator를 반복 시키는 역할도 한다.**

### MyIterator

반복자가 어떻게 작동하는지 더 잘 이해하기 위해서 `ArrayList<String>` 를 예시 든다.

```java
/**
 * A MyIterator is a mutable object that iterates over
 * the elements of an ArrayList<String>, from first to last.
 * This is just an example to show how an iterator works.
 * In practice, you should use the ArrayList's own iterator
 * object, returned by its iterator() method.
 */
public class MyIterator {

    private final ArrayList<String> list;
    private int index;
    // list[index] is the next element that will be returned
    //   by next()
    // index == list.size() means no more elements to return

    /**
     * Make an iterator.
     * @param list list to iterate over
     */
    public MyIterator(ArrayList<String> list) {
        this.list = list;
        this.index = 0;
    }

    /**
     * Test whether the iterator has more elements to return.
     * @return true if next() will return another element,
     *         false if all elements have been returned
     */
    public boolean hasNext() {
        return index < list.size();
    }

    /**
     * Get the next element of the list.
     * Requires: hasNext() returns true.
     * Modifies: this iterator to advance it to the element 
     *           following the returned element.
     * @return next element of the list
     */
    public String next() {
        final String element = list.get(index);
        ++index;
        return element;
    }
}
```

`MyIterator` 는 지금까지 작성한 클래스와 다른 몇가지 자바 언어기능을 사용한다.

1. **Instance variables**
- **필드라고도 불리는 `instance variables` 는 메소드 파라미터, 지역 변수와는 다르다.**
- `**instance variables` 는 객체 인스턴스에 저장되고 메서드 호출보다 오래 저장된다.**
- **위에서 인스턴스 변수는? list,index**

1. **constructor**
- **새로운 객체를 만들고 인스턴스 변수를 초기화한다.**
- **위에서 생성자는? MyIterator(list)**

**→ MyIterator의 메서드는 `static` 이 없다. 이는 `iter.next()` 처럼 객체의 인스턴스에서 호출되어야 하는 인스턴스 메서드라는 것을 알려준다.**

`**this` 키워드는 instance object를 참조하기 위해, 특히 인스턴스 변수(`this.list`)를 참조하기 위해서 사용된다.**

**→ 이는 두개의 list(`instance variable` 그리고 `constructor parameter` )를 구분하기 위해서 사용한다.**

**거의 대부분이 this를 사용하지만 생략됨 ex) Index → this.index**

`**private` 은 객체 내부 상태와 내부 도움을 주는 메서드에 사용되는 반면에 `public` 는 클래스와 클라이언트를 위한 메서드와 생성자를 나타낸다.**

`**final` 는 객체 내부 상태중 어느 부분이 변경 가능하고 어느 부분이 변경 할 수 없는지를 나타낸다.**

**→ index는 `next()` 할 때 마다 없데이트해야 하기 때문에 변경이 가능하다. 하지만 `list` 는 전체 생명 기간동안 동일한 목록을 계속 가르켜야 하기 때문에 다른 `list`를 반복하기 위해서는 새로운 `iterator` 객체를 만들어야한다.**

**MyIterator 객체 다이어그램:**

![http://web.mit.edu/6.005/www/fa15/classes/09-immutability/figures/iterator.png](http://web.mit.edu/6.005/www/fa15/classes/09-immutability/figures/iterator.png)

**list 화살표를 이중으로 하여 `final`인 것을 나타낸다. 즉 , 화살표는 그려지면 바뀔 수 없다.**

**그러나 `ArrayList`객체는 변경이 가능하다. list를 final로 선언해도 영향을 미치지 않는다.**

**iterator은 왜 존재할까? 다양한 종류의 내부 표현을 가진 다양한 종류의 컬렉션 데이터가 있다.**

**모든 항목에 접근 할 수 있는 단일한 방법을 허용하므로, 클라이언트 코드가 더 간단해지고 컬렉션 구현을 반복하는 클라이언트 코드를 변경하지 않고도 컬렉션 구현을 변경 할 수 있다.**

---

## ****Mutation undermines an iterator****

간단한 작업을 `iterator` 을 사용해서 한다. 우리가 `["6.005","8.03","9.00"]` 목록이 있다고 가정할 때

우리는 list에서 6 과목을 삭제하고 다른 과목을 남겨두는 `dropCourse6` 메서드를 원한다. 

**Spec**

```java
/**
 * Drop all subjects that are from Course 6. 
 * Modifies subjects list by removing subjects that start with "6."
 * 
 * @param subjects list of MIT subject numbers
 */
public static void dropCourse6(ArrayList<String> subjects)
```

`**dropCourse6` 스펙에는 이 메서드가 파라미터를 변경 할 수 있다는 것을 경고한다.**

**다음으로 입력 공간을 분할하는 테스트 전략을 결정한 뒤 해당 분할을 포함할 테스트 케이스를 선택한다.(테스트 우선 프로그래밍)**

```java
// Testing strategy:
//   subjects.size: 0, 1, n
//   contents: no 6.xx, one 6.xx, all 6.xx
//   position: 6.xx at start, 6.xx in middle, 6.xx at end

// Test cases:
//   [] => []
//   ["8.03"] => ["8.03"]
//   ["14.03", "9.00", "21L.005"] => ["14.03", "9.00", "21L.005"]
//   ["2.001", "6.01", "18.03"] => ["2.001", "18.03"]
//   ["6.045", "6.005", "6.813"] => []
```

**마지막으로 구현한다.**

```java
public static void dropCourse6(ArrayList<String> subjects) {
    MyIterator iter = new MyIterator(subjects);
    while (iter.hasNext()) {
        String subject = iter.next();
        if (subject.startsWith("6.")) {
            subjects.remove(subject);
        }
    }
}
```

**그 뒤, 테스트 케이스를 작동하면 잘되지만 마지막 테스트 케이스는 실패한다.**

```java
// dropCourse6(["6.045", "6.005", "6.813"])
//   expected [], actual ["6.005"]
```

이건 Iterator의 단순한 버그가 아니다. 

**이것에 for 루프에 적용되는 Syntactic sugar도 마찬가지로 발생함(다른 코드를 사용한 경우)**

```java
for (String subject : subjects) {
    if (subject.startsWith("6.")) {
        subjects.remove(subject);
    }
}
```

**→ `ConcurrentModificationException` 이 발생한다. 내장된 iterator은 list가 변경되는 것을 감지하고 예외를 던진다.**

**이 문제를 어떻게하면 해결할 수 있을까? Iterator의 `remove()` 메소드를 이용해서 해당 인덱스를 적절하게 조정해야한다.**

```java
Iterator iter = subjects.iterator();
while (iter.hasNext()) {
    String subject = iter.next();
    if (subject.startsWith("6.")) {
        iter.remove(subject);
    }
}
```

`**iter.remove()` 는 해당 원소를 어디서 삭제해야할지 알고있는 반면에 `subject.remove()` 는 다시 처음부터 검색해야하기 때문에 iter를 지우는게 더 효율적이라는 것을 알 수 있다.**

**그러나 이것은 문제를 해결하는 것은 아니다. 동일한 리스트에 대해 다른 `iterator` 가 있다면 정보가 전달되지는 않을 것이다.**

---

## Mutation and contracts

### ****Mutable objects can make simple contracts very complex****

**이는 mutable data structure의 근본적인 문제이다. 동일한 가변 객체(개체에 대한 `aliases` 라고도 함)에 대한 여러 참조는 프로그램의 여러 위치에서 일관성을 유지하기 위해서 해당 객체에 의존하고 있음을 알 수 있다.**

- **스펙 측면에서 봤을 때 계약은 더이상 한 곳(클라이언트와 구현자 사이)에서만 시행 될 수 없다. 이제 변경 가능한 객체와 관련된 계약은 `mutable object` 의 참조를 가진 사람들에 올바른 행동에 따라 달라진다.**
- **이런 지역화되지 않는 현상의 증상으로 Java 컬렉션 클래스가 있다. 이 클래스는 클라이언트와 클래스의 구현자에 대한 매우 명확한 계약으로 이루어졌다.**

→ 클라이언트의 요구사항에서 컬렉션을 반복하는 동안에 컬렉션을 수정할 수 없다는 문서가 있는가? 과연 누가 책임 지는지 알 수 있을까? `Iterator`? `List` ? `Collection` ?

**이와 같이 전역 속성에 대해서 생각을 해야하기 때문에 mutable data structure를 가진 프로그램에 정확성을 이해하고 확신하는 것은 훨씬 더 어렵다. 성능과 편의성을 위해서 이 작업을 계속 해야하지만 버그 안정성에 대해서 큰 비용을 지불해야한다.**

### ****Mutable objects reduce changeability****

`**Mutable object` 는 클라이언트와 구현자 사이의 계약을 더 복잡하게 만들고 클라이언트와 구현자에게 변경 자유를 줄인다.
→ 즉 변경이 허용된 객체를 사용하기에는 코드 변경이 더 어려워진다.** 

**예제의 핵심은 MIT 데이터베이스에서 사용자 이름을 검색하고 사용자의 9자리 ID를 반환하는 이 방법의 스펙이다.**

```java
/**
 * @param username username of person to look up
 * @return the 9-digit MIT identifier for username.
 * @throws NoSuchUserException if nobody with username is in MIT's database
 */
public static char[] getMitId(String username) throws NoSuchUserException {        
    // ... look up username in MIT's database and return the 9-digit ID
}
```

**합리적인 스펙이다. 이제 이 메서드를 사용해서 사용자의 ID를 출력하는 클라이언트가 있다고 가정하자.**

```java
char[] id = getMitId("bitdiddle");
System.out.println(id);
```

**클라이언트와 구현자 모두 개별적으로 변경을 결정한다. 클라이언트는 사용자의 개인정보가 걱정돼어 ID처음 5자리를 가리기로 결정했다.**

```java
char[] id = getMitId("bitdiddle");
for (int i = 0; i < 5; ++i) {
    id[i] = '*';
}
System.out.println(id);
```

구현자는 데이터베이스의 속도와 부하를 걱정하기 때문에 검색한 사용자 이름을 기억하는 캐시를 도입한다.

```java
private static Map<String, char[]> cache = new HashMap<String, char[]>();

public static char[] getMitId(String username) throws NoSuchUserException {        
    // see if it's in the cache already
    if (cache.containsKey(username)) {
        return cache.get(username);
    }

    // ... look up username in MIT's database ...

    // store it in the cache for future lookups
    cache.put(username, id);
    return id;
}
```

**이 두가지 변경으로 인해 미묘한 버그가 생긴다. 클라이언트가 “bitdiddle”를 조회하고 char 배열을  다시 가져오면 클라이언트와 구현자가 동일한 char 배열을 가지게 된다.**

→ 배열에 대한 aliased(별칭)가 저장된다.

**클라이언트의 애매한 코드가 실제로 캐시의 ID를 덮어쓰게 되므로 getMitId(”bitdiddle”)에 대한 호출은 “928432033”과 같은 9자리 숫자를 반환하는게 아니라 “*****2033”으로 반환하게 된다.**

**변경 가능한 객체를 공유하면 계약이 복잡해진다. 여기서 책임은 누구한테 있을까?**

**클라이언트는 객체를 수정하지 말아야 할 의무가 있는가?**

**구현자는 반환하는  객체를 보유하지 말아야 할 의무가 있는가?**

**사양을 명확히 할 수 있는 방법은 한가지가 있다.**

```java
public static char[] getMitId(String username) throws NoSuchUserException 
  requires: nothing
  effects: returns an array containing the 9-digit MIT identifier of username,
             or throws NoSuchUserException if nobody with username is in MIT’s
             database. Caller may never modify the returned array.
```

**이것은 나쁜 방법이다. 이러한 접근 방식의 문제점은 이러한 스펙이 프로그램이 나머지 전부에 대해 유효해야 한다는 것을 의미한다.** 

**호출 직전에 `precondition`과 호출 직후의 `postcondition` 을 생각하면 나머지 시간동안 무슨일이 일어날지 생각 할 필요 없다.**

**비슷한 문제가 있는 스펙**

```java
public static char[] getMitId(String username) throws NoSuchUserException 
  requires: nothing
  effects: returns a new array containing the 9-digit MIT identifier of username,
             or throws NoSuchUserException if nobody with username is in MIT’s
             database.
```

**이렇게 해도 문제를 완전히 해결 할 순 없다. 이 스펙에서는 구현자가 해당 배열을 변경하거나 다른 용도로 재사용하는 것을 방지 할 수 없다.**

**훨씬 더 나은 스펙**

```java
public static String getMitId(String username) throws NoSuchUserException 
  requires: nothing
  effects: returns the 9-digit MIT identifier of username, or throws
             NoSuchUserException if nobody with username is in MIT’s database.
```

`**immutable type`인 String 반환 값은 클라이언트와 구현자가 char 배열을 사용할 때 처럼 서로 같은 배열을 가르키지 않도록 보장한다.프로그래머가 스펙 코멘트를 자세히 읽는지는 상관 x**

**문자열을 변경 할 수 없을뿐만 아니라 이전 접근 방식과 다르게 구현자에게 캐시를 자유롭게 도입 할 수 있게한다.**

---

## ****Useful immutable types****

`**immutable type`은 많은 함정을 피해서 Java API에서 일반적으로 사용되는 불변 유형이 있음**

- `**primitive type` 과 `primitive wrappers` 는 `immutable` 이다. 큰 숫자로 계산 해야 할 때 사용하는 `BigInteger` 과 `BigDecimal` 은 `immutable` 이다.**
- **변경이 가능한 `Date` 를 사용하지말고, 필요한 시간 기록을 해서 적절하게 변경 불가능한 유형인 `java.time`을 사용해라**
- **Java의 컬렉션 유형(`List` , `Set` , `Map`)은 모두 변경가능하다. `ArrayList` , `HashMap` 등등 컬렉션 유틸리티 클래스에는 이러한 변경 가능한 컬렉션에 대한 변경 불가능한 뷰를 얻을 수 있는 메서드가 있다.**
    - `Collections.unmodifiableList`
    - `Collections.unmodifiableSet`
    - `Collections.unmodifiableMap`

**수정 불가능한 뷰를 list,set,map 의 wrraper로 생각 할 수 있다. Wrapper에 대한 참조가 있고 수정(`add`, `remove`, `put`등)을 수행하려고 시도한다면 `UnsupportedOpreationException` 이 발생 한다.**

**변경 가능한 컬렉션을 다른 프로그램으로 전달하기전에 `unmodifiable wrapper` 로 감싸 만들 수 있다.** 

**→ 그렇기 위해서는 우리는 실수로 컬렉션을 변경하지 않기 위해서 변경 가능한 컬렉션에 대한 reference를 잊어버려야한다.** 

**final이 붙은 객체 또한 변경 가능한 것처럼 수정 불가능한 래퍼 내부의 변경 가능한 컬렉션을 누군가가 수정 할 수 있기때문에.**

- `**Collections` 는 또한 변경이 불가능한 컬렉션도 제공한다.(`Collections.emptyList` 등) 확실히 비어있는 List가 갑자기 비어있지 않는 것을 발견하는 것이 제일 좋다.**

---

## Summary

**우리는 `mutable type`이 성능과 편의성에 유용하지만 객체를 사용하는 코드가 전역 수준으로 작동하게 돼서 버그를 만들 수 있고 테스트와 추론을 힘들게 한다는 것을 확인함.**

**핵심 디자인 원칙은 `immutability` 이다. 불변 객체와 불변 참조를 가능한 많이 사용해라.** 

- **SFB :** 불변 객체는 aliasing로 인한 버그를 막아준다. 불변 참조를 항상 동일한 객체를 가르킨다.
- **ETU :** 불변 객체나 참조는 항상 같은 것을 의미하기 때문에 코드를 읽기 쉽다. 변경 할 수 없기 때문에 객체나 참조가 변경 될 수 있는 모든 위치를 찾을 필요가 없다.
- **RFC** : 런타임시 개체나 참조를 변경할 수 없는 경우 해당 개체나 참조에 의존하는 코드는 프로그램이 변경될 때 수정 할 필요가 없습니다.