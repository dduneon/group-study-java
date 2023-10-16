# Recursive Data Types

## Objectives

- recursive datatype 이해
- 데이터 유형 정의 읽고 쓰기
- recursive data type에 대한 function 이해 및 구현
- immutable lists를 이해하고 그에 대한 표준 작업을 알아보기
- ADT를 사용해서 프로그램을 작성하는 방법

---

## ****Recursive functions****

**데이터와 계산 모두의 재귀적 구조를 갖는 recursive data type을 소개하기전에 recursive computation을 검토해라.**

**`recursive function`이 자체적으로 정의되는 것처럼, `recursive data type`도 자체적으로 정의된다.** 

**→ 우리는 abstract type의 다른 변형으로 나타나는 recursive case 및 base cases를 보게 될 것이다.**

---

## ****Immutable lists****

고전적인 recursive datatype인 immutable list부터 시작할 것이다.

**immutablity는 안정성뿐만 아니라 공유 가능성 때문에 강력하다. 공유는 실제로 성능상의 이점을 제공한다.**

→ 메모리 소비량이 적고 복사에 소요되는 시간이 줄어든다. 

**여기서는 익숙한 ArrayList나 LinkedList와 다르게 List data structure를 표현하는 방법을 보겠다.**

**immutable list `ImLIst<E>` 의 data type를 정의 해보겠다.4가지 작업이 있음**

- **empty: void → ImList // 빈 목록을 반환한다.**
- **cons: E x ImList → ImList // 다른 목록 앞에 원소를 추가하여 만든 새로운 List를 반환한다.**
- **first: ImList → E // 목록의 첫 번째 원소를 반환한다. 목록은 비어있으면 안된다.**
- **rest: ImList → ImList // 첫 번째 원소를 제외한 모든 원소를 List로 반환. 목록은 비어있으면 안됨**

**이러한 4가지 동작을 함수형 프로그래밍에서 널리 사용되며 FIrst, Rest 대신 Head 및 Tail이라고 불림**

**이 데이터 유형을 구현하기 위해서 Java 클래스를 설계하기전에 Int List를 사용하여 동작에 익숙해지도록 하겠음. [1, 2, 3] 과 같이 대괄호로 List를 작성하고 마치 수학 함수인 것 처럼 연산을 작성한다.** 

**Java를 사용하면 구문은 다르게 보이지만 동작의 의미는 동일하다.**

> empty() = []
cons(0, empty()) = [0]
cons(0, cons(1, cons(2, empty())) = [0, 1, 2]
x = cons(0, cons(1, cons(2, empty()))) = [0, 1, 2]
first(x) = 0
rest(x) = [1, 2]
first(rest(x)) = 1
rest(rest(x)) = [2]
first(rest(rest(x))) = 2
rest(rest(rest(x))) = []
> 

first, Rest 및 cons간의 기본 관계는 다음과 같다

> first(cons(elt,lst)) = elt
rest(cons(elt, lst)) = lst
> 

`cons` 는 합치는 것, `first` `rest` 는 벗겨내는 것

### Immutable lists in Java

Java에서 이 데이터 유형을 구현하기 위해 인터페이스를 사용한다.

```java
public interface ImList<E> {
	//TODO: ImLIst<E> empty()
	public ImLise<E> cons(E e);
	public E first();
	public ImList<E> rest();
}
```

**이 인터페이스는 `ImList<Integer>` , `ImList<String>` 등 모든 유형 E에 대해 인스턴스화 할 수 있는 `ImList<E>`를 선언한다.** 

**이러한 `E` 선언은 컴파일러가 코드의 타입 안정성을 확인 할 때 채워질 자리 표시자이다.**

**그리고 이 인터페이스를 구현하는 두 개의 클래스를 작성하겠다.**

- `**Empty` : 빈 작업의 결과를 나타낸다.(empty list)**

```java
public class Empty<E> implements ImList<E> {
    public Empty() {
    }
    public ImList<E> cons(E e) {
        return new Cons<>(e, this);
    }
    public E first() {
        throw new UnsupportedOperationException();
    }
    public ImList<E> rest() {
        throw new UnsupportedOperationException();
    }
}
```

- `**Cons` : cons 연산의 결과를 나타낸다.(다른 List와 함께 붙어 있는 원소)**

```java
public class Cons<E> implements ImList<E> {
    private final E e;
    private final ImList<E> rest;

    public Cons(E e, ImList<E> rest) {
        this.e = e;
        this.rest = rest;
    }
    public ImList<E> cons(E e) {
        return new Cons<>(e, this);
    }
    public E first() {
        return e;
    }
    public ImList<E> rest() {
        return rest;
    }
}
```

**cons, first, rest에 대한 메서드는 구현했는데 데이터 유형의 네번째 연산인 Empty는 어디에 있을까?**

**Empty를 구현하는 한 가지 방법은 클라이언트가 클래스 생성자를 호출하여 `Empty` List를 얻도록 하는 것이다.**

**이는 표현 독립성을 희생한다. 클라이언트는 `Empty`클래스에 대해 알아야 한다.**

**인터페이스에서 본 것 처럼 이를 수행하는 더 좋은 방법은 인자를 사용하지 않고 `Empty` 인스턴스를 생성하는 `static factory method` 를 사용하는 것이다.** 

이 static method를 다른 메서드와 함께 `ImList` 인터페이스에 넣을 수 있다. 이전 Java에서는 그런 선택이 불가능해서 이러한 코드를 작성해야 한다.

```java
List<String> z = new ArrayList<>();
```

`List.empty()` 에서는 언젠가 List에 대한 새로운 값을 얻는 방법을 제공하겠지만 아직은 아니다.

```java
public interface ImList<E> {
	public static <E> ImList<E> empty(){
		return new Empty<>();	
	}
	public ImLise<E> cons(E e);
	public E first();
	public ImList<E> rest();
}
```

`**empty` 의 시그니처는 새로운 제네릭 타입 문법을 사용한다. `ImList<E>` 의 `E` 는 인스턴스의 요소 유형에 대한 대치자이지만 static이다.** 

→ 인스턴스 변수나 메서드를 볼 수 없으며 인스턴스 타입 매개변수도 볼 수 없다. 

`**empty` 의 선언을 이렇게 읽을 수 있다** : 모든 E가 `empty()` 인 `ImList<E>`

## ****Two classes implementing one interface(하나의 인터페이스를 구현하는 두개의 클래스)****

**이 디자인은 `ArrayList` , `LinkedList`,`List` 에서 본 디자인과 다르다.**

`**List` 는 abstract data type이며 `ArrayList` 와 `LinkedList` 는 해당 데이터 유형에 대한 두 가지 대체 표현이다.**

`**ImList` 의 경우 data type을 구현하기 위해서는 `Empty` 및 `Cons` 협력이 모두 필요하다.**

---

## ****Recursive datatype definitions****

**Abstract data type `ImList` 와 두 개의 구체적인 클래스 `Empty` , `Cons`는 recursive datatype를 형성한다.**

`**Cons` 는 `ImList` 의 구현이지만 자체적으로 rep 내부(`rest`필드에 대한)를 제공하므로 계약을 성공적으로 구현하기 위해서는 재귀적으로 `ImList`구현이 필요하다.**

이 사실을 명확하게 표시하기 위해서 datatype definition을 작성한다.

```java
ImList<E> = Empty + Cons(first:E, rest:ImList)
```

**이는 `ImList`의 값 집합으로서의 재귀적 정의이다. `ImList` 집합은 `Empty`생성자를 요소 및 `ImList` 에 적용하는 두 가지 방법으로 형성된 값으로 구성된다.**

**→ 이러한 방식으로 작성하면 datatype의 재귀적 특성이 훨씬 잘 드러난다.**

또한 이 정의를 사용하여 `ImList` 값을 용어나 표현식으로 작성할 수 있다.

```java
Cons(0, Cons(1, Cons(2, Empty)))
```

공식적으로 데이터 타입 정의는 다음과 같다.

- **왼쪽의 abstract data type, 오른쪽에 representation(or concrete datatype)에 의해 정의됨**
- **representation은 +로 구분된 데이터 타입의 variants으로 구성된다.**
- **각 변형은 0개 이상의 명명된(및 형식화된) 인수가 있는 생성자다.**

**또 다른 예는 이진 트리이다.**

```java
Tree<E> = Empty + Node(e:E, left:Tree<E>, right:Tree<E>)
```

---

## ****Functions over recursive datatypes****

**구체적인 변형이 있는 abstract datatype의 재귀적 정의인 데이터 유형에 대한 이러한 사고 방식은 List 및 트리와 같은 재귀적이고 제한되지 않은 구조를 처리할 수 있을 뿐만 아니라 데이터 유형에 대한 작업을 설명하는 편리한 방법을 제공한다는 점에서 매력적이다. → variant 당 하나의 케이스가 있는 함수**

**ex) List의 Size를 고려해봐라. 이는 확실히 ImList에서 우리가 원하는 작업이다. 우리는 이를 다음과 같이 정의할 수 있다.**

**size : ImList → int** // 목록의 크기를 반환한다.

그런 다음 `ImList` 의 variant에 대한 크기를 정의하여 그 의미를 완전히 지정한다.

> size(Empty) = 0
size(Cons(first: E, rest: ImList)) = 1 + size(rest)
> 

**이 함수는 재귀적이다. 특정 목록의 크기 실행을 일련의 reduction steps(축소 단계)로 생각할 수 있다.**

> size(Cons (0, Cons( 1, EMpty)))
= 1 + size(Cons (1, Empty))
= 1 + (1 + size(Empty))
= 1 + (1 + 0)
= 1 + 1
= 2
> 

그리고 정의의 사례는 `ImList` , `Empty` , `Cons` 의 메서드로 자바로 직접 변환 될 수 있다.

```java
public interface ImList<E> {
    // ...
    public int size();
}

public class Empty<E> implements ImList<E> {
    // ...
    public int size() { return 0; }
}

public class Cons<E> implements ImList<E> {
    // ...
    public int size() { return 1 + rest.size(); }
}
```

재귀적 데이터 유형에 대한 작업을 구현하는 이 패턴은 다음과 같다.

- **abstract datatype 인터페이스에서 operation 선언**
- **각 구체적인 variant에서 작업을(재귀적으로) 구현**

매우 일반적이고 실용적인 디자인 패턴이다. 때로는 도움이 되지 않는 interpreter pattern을 따르기도 한다.

몇가지 예를 더 시도해본다.

**isEmpty : ImList → boolean**

> isEmpty(Empty) = true
isEmpty(Cons(first: E, rest: ImList)) = false
> 

**contains : ImList x E → boolean**

> contains(Empty, e: E) = false
contains(Cons(first: E, rest: ImList),e : E) = (first = e) v contains(rest, e)
> 

**get : ImList x int → E**

> get(Empty, e: E) = undefined
get(Cons(first: E, rest: ImList), n) = if n = 0 then first else get(rest, n - 1)
> 

**reverse : ImList → ImList**

> reverse(Empty) = empty()
reverse(Cons(first: E, rest: ImList)) = append(reverse(rest), cons(first, empty())
> 

**reverse의 경우 재귀적 정의 Java에서 매우 나쁜 구현을 생성하고 reverse list length에 따라 성능이 2차적인 것으로 나타난다. 필요한 경우 iterative 접근 방식을 사용해서 다시 작성할 수 있다.**

---

## ****Tuning the rep****

List의 크기를 얻는 것은 일반적인 작업이다. 현재 우리의 `size()` 구현에는 O(n) 시간이 걸린다. 여기서 n은 List의 길이이다. 이는 목록 항목 수에 있어서 선형이다. 처음 계산할 때 크기를 캐시하는 List representation을 간단히 변경하면 더 나은 결과를 얻을 수 있으므로 이후에는 O(1) 시간(List 원소의 수와 관계 없이 상수 시간)만 소요된다.

```java
public class Cons<E> implements ImList<E> {
    private final E e;
    private final ImList<E> rest;
    private int size = 0;
    // rep invariant:
    //   e != null, rest != null, size >= 0
    //   size > 0 implies size == 1+rest.size()

    // ...
    public int size() { 
        if (size == 0) size = 1 + rest.size();
        return size;
    }
}
```

**아직 크기가 계산하지 않았음을 나타내기 위해서 특수 값 0(Cons 크기가 될 수 없음)을 사용하고 있다. 또한 이러한 변경으로 인해서 `size` 필드를 `rest` 필드에 연결하는 `rep invariant`에 새로운 절이 도입되었습니다.**

**immutable datatype이면서도 mutable representation를 갖고 있다. 이 경우 비용이 많이 드는 작업의 결과를 캐시하기 위해 자체 크기 필드를 수정한다. 이는 객체가 나타내는 추상 값을 변경하지 않는 상태 변경인 유익한 mutation의 예이므로 type은 여전히 불변이다.**

### ****Rep independence and rep exposure revisited****

`**ImList` 의 Java 구현은 여전히 rep independence를 갖고 있는가? 우리는 정적 메소드 `imList.empty()` 뒤에 `Empty ctor` 를 숨겼으며 클라이언트는 결코 `Empty` , `Cons` 를 직접 사용 할 필요가 없다.**

`**ImList` 패키지 외부의 클래스에서 이를 보거나 사용할 수 없도록 패키지 전용(`public` `private` 로 선언 x)으로 만들어 더 숨길 수 있다.**

**우리는 구현을 자유롭게 변경할 수 있다. 실제로 `Cons` 내부에 `Size` 필드를 추가했다. `get()`을 더 빠르게 실행하기 위해서 배열을 추가 할 수 있다. 그러나 공간적으로 비용이 많이 들 수 있지만 그러한 절충을 자유롭게 만들 수 있다.**

`**Cons.rest()` 가 내부 list의 참조값을 반환하기 때문에 rep 노출이 되는 것인가? 클라이언트가 실수로 `rest list`에 원소를 추가 할 수 있는가? 그렇다면 이는 `Cons` 의 immutablity 중 두 가지, 즉 불변성과 캐시된 크기가 항상 정확하다는 점을 위협한다. 그러나 내부 목록은 변경할 수 없으므로 rep가 노출될 위험은 없다.** 

→ 누구도 `Cons` 의 rep invariant를 위협할 수 없다.

---

## ****Null vs. empty****

`Empty` 클래스를 제거하고 대신 `null` 을 사용하고 싶을 수 있다. → 참아라

**Null참조 대신 obejct를 사용하여 데이터 구조의 base case 혹은 endpoint를 알리는 것은 *`sentinel objects`* 라고 불리는 디자인패턴이다.**

**→ 데이터 유형의 객체처럼 작동하므로 해당 객체에 대해 메서드를 호출할 수 있다는 것이 좋은 점임**

따라서 empty list에서도 `size()` 메서드를 호출할 수 있다. 빈 목록으로 `null` 이 표시된다면 그렇게 할 수 없으며 우리 코드는 다음과 같은 테스트로 가득차게 된다.

```java
if (lst != null) n = lst.size();
```

코드를 복잡하게 만들고 의미를 모호하게 하며 잊어버리기 쉽다. 훨씬 간단할수록 좋다.

```java
n = lst.size()
```

이는 `lst`가 `Empty` 객체를 참조하는 경우를 퐇마하여 항상 작동한다.

`null` 데이터 구조를 벗어나면 훨씬 편해질 것 이다.

---

## ****Declared type vs. actual type****

이제 인터페이스와 클래스를 사용하고 있으므로 Java의 유형 검사 작동 방식에 대한 중요한 점을 강조하는 시간을 가질 가치가 있다. 실제로 정적으로 검사되는 모든 객체 지향 언어는 이런 방식으로 작동한다.

`type checking`에는 프로그램이 실행되기 전의 컴파일 타임과 프로그램이 실행되는 런타임이 있다.

**컴파일 타임에는 모든 변수가 선언 될 때 가지는 `declared type` 를 가지고 있다.** 

**예를 들어 `new String()`는 실제 유형이 `String` 인 객체를 만든다. `new Empty()` 는 실제 유형이 `Empty`인 객체를 만든다.** 

→ **`new ImList()` 는 인터페이스이기 때문에 Java에서는 금지된다.(ImList는 객체 값도 없고 생성자도 없다)**

---

## ****Another example: Boolean formulas****

CS에서 유용한 또 다른 종류의 recursive datatype은 부울 공식이다. 예를 들어, 명제 논리의 공식은 다음과 같다.

> (P [∨](https://en.wikipedia.org/wiki/Logical_disjunction) Q) [∧](https://en.wikipedia.org/wiki/Logical_conjunction) ( [¬](https://en.wikipedia.org/wiki/Logical_negation) P [∨](https://en.wikipedia.org/wiki/Logical_disjunction) R)
> 

이는 P 또는 Q가 참이고 P가 거짓이거나 R이 참이다.를 의미한다.

명제 논리의 모든 공식을 표현하는 데 적합한 데이터 유형 정의를 제공할 수 있다.

```java
Formula = Variable(name:String)
          + Not(formula:Formula)
          + And(left:Formula, right:Formula)
          + Or(left:Formula, right:Formula)
```

*(P ∨ Q) ∧ (¬P ∨ R)* 은

```java
And( Or(Variable("P"), Variable("Q")),
     Or(Not(Variable("P")), Variable("R")) )
```

부울 수식의 핵심 작업은 만족할 수 있는지 여부, 즉 변수에 참/거짓 값을 할당하면 수식이 참으로 평가되는지 여부를 테스트하는 것이다. 만족도를 확인하는 간단하지만 느린 알고리즘이 있다.

1. 수식에서 변수 집합을 추출한다. 우리는 이미 변수 작업을 통해 이를 구현했다.
2. 해당 변수에 가능한 모든 참/거짓 값 할당을 시도해 보아라. 변수와 그 값의 List를 사용하여 할당을 나타낼 수 있다. 
    
    `Enviroment` (변수 및 변수 List)를 사용하여 할당을 나타낼 수 있다. `ImList` 를 사용하여 `Enviroment` 를 구현하거나 immutable map type을 개발할 수 있다.
    
3. 각 환경에 대한 공식을 평가한다.
    
    이를 위해 평가를 정의한다 : Formula x Environment → Boolean
    
4. 수식이 true로 평가되는 첫 번째 Environment를 반환한다.

이러한 조각을 정의하고 만족스러운 형태로 결합한다. : Formula → Boolean함수는 다음에 연습해보겠다.

### ****Backtracking search with immutability****

**우리는 서로 다른 목록 인스턴스 간에 많은 공유를 허용하는 표현인 immutable list로 시작했다.** 

**하지만 특정 종류의 공유는 끝 부분만 실제로 공유할 수 있다. 두 개의 List는 처음에는 동일했지만 나중에는 다른 경우 별도로 저장해야한다.**

**공백(부울 변수 집합에 대한 할당 공간과 같은)을 통한 검색은 일반적으로 하나를 선택하여 진행되며, 선택이 막다른 골목에 도달하면 되돌아간다.**

**mutable data structures는 일반적으로 백트래킹에 좋은 접근 방식이 아니다. 예를 들어 변경 가능한 `Map` 를 사용하여 시도중인 현재 변수 바인딩을 추적하기 위해 mutable을 사용하는 경우 백트래킹할 때 마다 해당 바인딩을 취소해야한다. → 백트래킹 할 때는 그냥 Map을 버리면 된다.**

**공유가 없는 immutable data structure도 좋은 생각은 아니다. 전체 복사본을 만들어야 하는 경우 현재 위치를 추적하는 데 필요한 공간이 2차적으로 증가하기 때문이다. 작업을 진행할 때 마다, 백업이 필요한 경우를 대비에 경로에 있는 모든 이전 환경을 유지해야한다.**

**immutable list에는 경로의 각 단계를 목록 앞에 추가하기만 하면 이전 단계의 모든 정보를 공유할 수 있다는 속성이 있다. 역추적해야하는 경우 현재 단계 상태 사용을 중지하지만 이전 단계에 대한 참조가 있다.**

**마지막으로, immutable data structure를 사용하는 검색은 즉시 병렬화할 준비가 된다. 공유된 변경 가능한 데이터 구조에서 서로 밝게 되는 문제를 처리할 필요 없이 여러 프로세서에 한 번에 여러 경로를 검색하도록 위임할 수 있다.** 

---

## Summary

recursive datatype에 대한 큰 아이디어 외에도 다음 내용을 읽었다.

- **datatype definitions(데이터 타입 정의)** : abstract type, 특히 재귀 유형에 대해 생각
- **functions over recursive datatypes(재귀 데이터 유형에 대한 함수) : 유형 스펙에 선언되고 구체적인 변형당 하나의 사례로 구현된다.**
- **immutable list(불변 목록) :** 불변 데이터 유형의 고전적이고 표준적인 예

언제나 그렇듯이, 우리는 이러한 아이디어가 어떻게 우리의 코드를 버그로부터 더 안전하게 만들고, 이해하기 쉽게하며, 변경에 더 잘 대비하게 만드는지 묻는다. 

---

# ****Writing a Program with Abstract Data Types****

## ****Recipes for programming****

**procedure(정적 메서드)작성을 위한 test-first programming(테스트 우선 프로그래밍)접근 방식을 떠올려보아라.**

1. **Spec :** 메소드 시그니처(이름, 파라미터, 리턴 타입, 예외)와 Precondition 및 postcondition을 포함한 스펙을 Javadoc 커멘트로 작성한다.
2. **Test** : 체계적인 테스트 케이스를 작성하고 이를 Junit에 넣어 자동으로 실행할 수 있다.
    1. 테스트 케이스 작성을 시작할 때 돌아가서 스펙을 변경해야 할 수도 있다. 클라이언트가 메서드를 호출하는 방법을 생각하고 있기 때문에 테스트 케이스를 작성하는 과정만으로도 스펙에 부담이된다.
    2. 처음에는 적어도 일부 테스트가 실패 하는지 확인해라. 메서드를 구현하지 않은 경우에도 모든 테스트를 통과하는 test suite는 버그를 찾는 데 좋은 테스트 모음이 아니다.
3. **implement** : 메서드의 바디를 작성한다. Junit 테스트가 모두 통과돼야함
    1. 메서드를 구현하면 테스트와 스펙에 모두 부담이 되고, 거기서 돌아가서 수정해야 할 버그를 발견할 수 있다. 따라서 작업을 완료하려면 구현, 테스트 및 스펙을 변경하고 그 사이를 왔다 갔다 해야 할 수 있다.

**이것은 abstract datatype 작성 방법으로 확장해보겠다.**

1. **Spec** : 메소드 시그니처, precondition, postcondition을 포함하여 데이터 타입의 작업에 대한 사양을 작성한다.
2. **Test** : ADT 작업에 대한 테스트 케이스를 작성한다.
    1. 스펙에 부담을 준다. 예상하지 못한 작업ㅇ이 필요하다는 사실을 발견할 수 있으므로 해당 작업을 스펙에 추가해야 한다.
3. I**mplement** : ADT의 경우 이 부분은 다음으로 확장됩니다.
    1. **Choose rep** : 클래스의 비공개 필드나 재귀 데이터 유형의 variant을 적어보세요. rep invariant을 주석으로 적어보세요
    2. **Assert rep invariant :** rep invariant를 구현하는 `checkRep()` 메소드를 구현한다. 이는 rep invariant이 줭요하지 않은 경우 매우 중요하다. 버그를 훨씬 일찍 발견할 수 있다.(failt fast)
    3. **Implement operations :** operations의 메서드 바디를 작성하고 반드시 그 안에 `checkRep()`를 호출해라. JUnit에서 테스트가 모두 녹색이면 완료된다.
    

그리고 이를 프로그램 작성 방법(ADT 및 프로시저로 구성)으로 더욱 확장해 보겠다.

1. **Choose datatypes(데이터타입을 선택)** : 어느것이 mutable이고 immutable인지 결정해라
2. **Choose procedures(절차를 선택해라)** : 최상위 수준 procedure를 작성하고 더 작은 단계로 나눈다.
3. **Spec** : ADT와 Procedure를 자세히 살펴보아라. 처음에는 ADT작업을 단순하고 적게 유지하고 필요할 때만 복잡한 작업을 추가해라
4. **Test :** 각 유닛(ADT 또는 프로시저)에 대한 테스트 케이스를 작성해라
5. **Implement simply first(일단 간단하게 구현) :** 단순하고 무차별적인 표현을 선택해라. 여기서 중요한 점은 스펙과 테스트에 압력을 주고 가능한 빨리 프로그램으로 모으는 것이다. 먼저 전체 프로그램이 올바르게 작동하도록 해라. 지금은 상위 수준의 기능과 성능 최적화등을 건너 뛰고 코너케이스를 건너 뛰어라. 다시 방문해 할 일등을 유지해라
6. **Reimplement and iterate and optimize(재구현하고 반복하고 최적화해라) :** 이제 모든 작업이 완료되었으므로 더 잘 작동하도록 리팩토링해라

---

## ****Problem: matrix multiplication****

행렬 곱셉을 계산하고 더 빠르게 수행하고 싶다고 가정해 보겠다.

예를 들어, a b가 스칼라 상수이고 X가 대형 행렬인 경우

> (aX)b
> 

는 행렬 X를 두번 반복하기 때문에 계산 속도가 느린다. 한 번은 a를 곱하고 다시 b를 곱한다. 다음을 수행하는 것이 동일하고 저렴할 것이다.

> (ab)X
> 

이는 행렬 곱셈에서 곱을 재배열하여 만들 수 있는 최적화의 한 예일 뿐이다.( 그러나 행렬 곱셉은 결합적이지만 교환적이지 않으며 스칼라만 교환된다는 점을 기억해라)

### ****Choose datatypes****

이것은 `MatrixExpression` 이라고 부르자. 정의를 더 쉽게 읽을수 있도록 줄여서 `MatExpr` 로 하겠다.

몇 가지 작업을 정의해 보겠다.

**make : double → MatExpr**

// effects : 주어진 스칼라만으로 구성된 표현식을 반환한다.

**make : dobuel[][] → MatExpr**

// requires : array.length > 0, array[i].lengths는 모든 i에 대해 동일하고 > 0 이다.

// effects : 주어진 행렬만으로 구성된 표현식을 반환한다.

**times : MatExpr x MatExpr → MatExpr**

// requires : m1과 m2는 곱셉에 호환된다.

// effects : m1 x m2를 반환한다.

**isIdentity : MatExpr → boolean**

// effects : 표현식이 곱셈 항등식인 경우 true를 반환한다.

그리고 우리가 정말로 원하는 것:

**optimize : MatExpr → MatExpr**

// effects : 동일한 값을 가진 표현식을 반환하지만 계산 속도가 더 빠를 수 있다.

### Test

`optimize()` 를 테스트해보자

- 표현식의 스칼라 수 : 0, 1, 2, > 2
- 표현식 트리의 스칼라 위치: 바로 왼쪽, 바로 오른쪽, 왼쪽 왼쪽, 오른쪽 왼쪽, 왼쪽 오른쪽, 오른쪽 오른쪽

| 테스트 케이스 | 적용되는 파티션 |
| --- | --- |
| X ⇒ x | 스칼라 0개 |
| AX ⇒ AX | 1스칼라 바로 왼쪽 |
| a(Xb) ⇒ (ab)X | 스칼라 2개, 바로왼쪽, 오른쪽 - 오른쪽 |
| (aX)b ⇒ (ab)X | 스칼라 2개, 바로 오른쪽, 왼쪽 - 왼쪽 |
| (Xa)(bY) ⇒ ((ab)X)Y) | 스칼라 2개, 왼쪽 - 오른쪽, 오른쪽 - 왼쪽 |

### Choose a rep

이 문제는 재귀적 데이터 유형에 있어서 자연스러운 문제이다.

> MatExpr = Identity + Scalar(이중) + Matrix(이중[][]) + Product(MatExpr, MatExpr)
> 

```java
/** Represents an immutable expression of matrix and scalar products. */
public interface MatrixExpression {
    // ...
}

class Identity implements MatrixExpression {
    public Identity() {
    }
}

class Scalar implements MatrixExpression {
    private final double value;
    public Scalar(double value) {
        this.value = value;
    }
}

class Matrix implements MatrixExpression {
    private final double[][] array;
    // RI: array.length > 0, and all array[i] are equal nonzero length
    public Matrix(double[][] array) {
        this.array = array; // note: danger!
    }
}

class Product implements MatrixExpression {
    private final MatrixExpression m1;
    private final MatrixExpression m2;
    // RI: m1's column count == m2's row count, or m1 or m2 is scalar
    public Product(MatrixExpression m1, MatrixExpression m2) {
        this.m1 = m1;
        this.m2 = m2;
    }
}
```

### Choose an identity

`null` 을 사용하지 않도록 데이터 유형에 아무것도 나타내지 않는 값을 갖는 것이 항상 좋다. 행렬 곱의 경우 자연스러운 선택은 항등 행렬이다. 빈 곱 표현식은 어쨌든 항등일뿐이다. 그럼 다음과 같이 정의해 보겠다.

```java
/** Identity for all matrix computations. */
public static final MatrixExpression I = new Identity();
```

불행하게도 이것이 완벽한 상황은 아니라는 것은 알게 될 것이다. 다른 MatExpr도 항등일 수 있다.

### ****Implementing `make` : use factory methods**

`maek()` 생성자부터 시작하여 구현을 시작하겠다.

**우리는 클라이언트가 보여지는 것에 의존하지 않고 나중에 변경할 수 있도록 concreate rep class인 `Scalar` , `Matrix` , `Product` 를 노출하고 싶지 않다.(변경 준비)**

따라서 우리는 `MatrixExpression` 을 구현하기 위해서 정적 메소드 `make()` 를 생성할 것이다.

```java
/** @return a matrix expression consisting of just the scalar value */
public static MatrixExpression make(double value) {
    return new Scalar(value);
}

/** @return a matrix expression consisting of just the matrix given */
public static MatrixExpression make(double[][] array) {
    return new Matrix(array);
}
```

이를 팩토리 메소드(factory method)라고 하며 생성자 역할을 하는 정적 메소드이다. 팩토리 메서드 패턴은 여러 언어의 객체 지향 프로그래밍에서 볼 수 있는 일반적인 디자인 패턴이다.

### ****Implementing `isIdentity` : don’t use `instanceof`**

이제 `isIdentity` observer를 구현해보겠다. 먼저 안좋은 코드는 이런식으로 작성한다.

```java
// don't do this
public static boolean isIdentity(MatrixExpression m) {
    if (m instanceof Scalar) {
        return ((Scalar)m).value == 1;
    } else if (m instanceof Matrix) {
        // ... check for 1s on the diagonal and 0s everywhere else
    } else ... // do the right thing for other variant classes
}
```

일반적으로 객체 지향 프로그래밍에서 `instanceof` 를 사용하는 것은 나쁜 냄새다. 작업을 각 클래스에 적합한 조각으로 나누고 대신 인스턴스 메서드를 작성한다.

```java
class Identity implements MatrixExpression {
    public boolean isIdentity() { return true; }
}

class Scalar implements MatrixExpression {
    public boolean isIdentity() { return value == 1; }
}

class Matrix implements MatrixExpression {
    public boolean isIdentity() { 
        for (int row = 0; row < array.length; row++) {
            for (int col = 0; col < array[row].length; col++) {
                double expected = (row == col) ? 1 : 0;
                if (array[row][col] != expected) return false;
            }
        }
        return true;
    }
}

class Product implements MatrixExpression {
    public boolean isIdentity() { 
        return m1.isIdentity() && m2.isIdentity();
    }
}
```

`**isIdentity` 를 구현하면 테스트 케이스 작성을 통해 먼저 발견했어야 하는 문제가 노출된다.**

**(값이 항등 항렬(ex. A x A-1)인 `Product` 에 대해 항상 true를 반환하지 않는다.)**

**→  `isIdentity` 의 스펙을 약화하여 이를 해결하고 싶어할 것이다.**

### ****Implementing `optimize` without `instanceof`**

`optimize()` 를 구현해보자. 다시 말하지만 여기에 우리를 혼란스럽게하는 나쁜 방법이 있다.

```java
// don't do this
class Product implements MatrixExpression {
    public MatrixExpression optimize() {
        if (m1 instanceof Scalar) {
            ...
        } else if (m2 instanceof Scalar) {
            ...
        }
        ...
}
```

**여기저기서 `instanceof` 가 포함된 코드를 작성하고 있다면 문제를 다시 생각해봐야한다.**

**특히 스칼라를 최적화하려면 두 개의 재귀 도우미 작업이 필요하므로 이를 abstract datatype에 추가한다.**

**scalars : MatExpr → MatExpr**

// effects : 행렬이 없고 스칼라만 포함된 MatExpr반환한다.

**matrices : MatExpr → MatExpr**

// effects : 스칼라가 없는 MatExpr만 반환하고 입력 표현식에 나타난 것과 동일한 순서에 행렬만 반환한다.

이러한 표현식을 사용하면 표현식에서 스칼라를 가져와 단일 곱셉 표현식으로 함께 이동할 수 있다.

```java
/** Represents an immutable expression of matrix and scalar products. */
public interface MatrixExpression {

    // ...

    /** @return the product of all the scalars in this expression */
    public MatrixExpression scalars();

    /** @return the product of all the matrices in this expression.
     * times(scalars(), matrices()) is equivalent to this expression. */
    public MatrixExpression matrices();
}
```

**구현한다면**

```java
class Identity implements MatrixExpression {
    public MatrixExpression scalars() { return this; }
    public MatrixExpression matrices() { return this; }
}
class Scalar implements MatrixExpression {
    public MatrixExpression scalars() { return this; }
    public MatrixExpression matrices() { return I; }
}
class Matrix implements MatrixExpression {
    public MatrixExpression scalars() { return I; }
    public MatrixExpression matrices() { return this; }
}
class Product implements MatrixExpression {
    public MatrixExpression scalars() {
        return times(m1.scalars(), m2.scalars());
    }
		public MatrixExpression matrices() {
        return times(m1.matrices(), m2.matrices());
    }
}
```

**이러한 helper functions를 사용하면 `optimize()` 는 쉽게 스칼라와 행렬을 분리할 수 있다.**

```java
class Identity implements MatrixExpression {
    public MatrixExpression optimize() { return this; }
}

class Scalar implements MatrixExpression {
    public MatrixExpression optimize() { return this; }
}

class Matrix implements MatrixExpression {
    public MatrixExpression optimize() { return this; }
}

class Product implements MatrixExpression {
    public MatrixExpression optimize() {
        return times(scalars(), matrices());
    }
}
```

---

## Summary

recursive datatype이 이 과정의 주요 목표에 어떻게 부합하는지 검토해볼 것이다.

- **SFB** : recursive datatype를 사용하면 재귀 등 구조의 문제를 해결할 수 있다. 중요한 작업을 캡슐화하고 자체 불변성을 유지하는 적절한 데이터 구조를 구현하는 것은 정확성을 위해 매우 중요하다.
- **ETU** : abstract type에 지정되고 각 구체적인 변형에 구현된 재귀 데이터 유형에 대한 함수는 유형의 다양한 동작을 구성한다.
- **RFC :** 다른 ADT와 마찬가지로 재귀 ADT는 추상 값을 구체적인 representation을 분리하므로 클라이언트를 변경하지 않고도 하위 수준 코드와 구현의 상위 수준 구조를 변경할 수 있다.