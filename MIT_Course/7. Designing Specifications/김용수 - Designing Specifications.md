# Designing Specifications

### 목표

- **결정된 스펙을 이해하고, 비결정성을 식별 및 평가할 수 있는 능력을 갖는 것**
- **선언적 스펙과 운영 스펙을 이해하고 선언적 스펙을 작성할 수 있다.**
- **preconditions와 postconditions 그리고 spec의 강도를 이해하고 스펙의 강도를 비교할 수 있다.**
- **적절한 강도의 일관성 있고 유용한 스펙을 작성할 수 있다.**

---

## Introduction

**유사한 행동을 하는 다양한 스펙을 보고 둘 사이에 장단점을 파악한다. 스펙 비교를 위한 3가지 관점**

- **얼마나 결정적인가? :** 스펙은 주어진 입력에 대해서 단일 가능 출력만을 정리하는가? 아니면 구현자가 나올 수 있는 결과값 집합 중 선택할 수 있는 것을 허용하는가?
- ********************************************얼마나 선언적인가? :******************************************** 스펙은 단지 출력값이 무엇인지 특징짓는 것인가? 아니면 출력을 계산하는 방법을 명시하는가?
- **얼마나 강한가? :** 스펙은 구현하는 부분에서 작은 집합을 차지하는가? 큰 집합을 차지하는가?

**모듈에 대해 선택할 수 있는 스펙들이 다 동등하게 좋은 것이 아니기 때문에 우리는 더 좋은 스펙을 알아보는 방법을  알아야한다.**

---

## Determinstic vs. underdetermined specs

**이전 공부에서 사용한 `find`의 두가지 구현 예시**

```java
static int find(First)(int[] arr, int val) {
    for (int i = 0; i < arr.length; i++) {
        if (arr[i] == val) return i;
    }
    return arr.length;
}
```

```java
static int find(Last)(int[] arr, int val) {
    for (int i = arr.length - 1 ; i >= 0; i--) {
        if (arr[i] == val) return i;
    }
    return -1;
}
```

`find` 뒤에 적힌 **(First)**와 **(Last)**는 실제로 자바 문법은 아니고 구별하기 위해서 적음. 실제 코드에서는 두 메서드는 `find` 로 불린다.

`**find`의 스펙은 이렇게 나올 수 있다.**

```java
static int findExactlyOne(int[] arr, int val)
  requires: val occurs exactly once in arr
  effects:  returns index i such that arr[i] = val
```

**이 스펙은 결정적이다:** Precondition을 만족하는 상태가 되면 결과는 완전히 결정된다. 하나의 return value와 하나의 최종 상태만 가능하다.

→ 둘 이상에 출력 값이 나오는 유효한 입력값은 존재하지않다.

`**find(First)` 와 `find(Last)` 모두 스펙을 만족한다. 그렇기 때문에 클라이언트가 의존하는 스펙이라면 두 구현은 서로 대체 될 수 있다.**

******************************약간 다른 스펙의 예시******************************

```java
static int findOneOrMore,AnyIndex(int[] arr, int val)
  requires: val occurs in arr
  effects:  returns index i such that arr[i] = val
```

**이 스펙은 결정적이지 않다:**  `val` 이 두번 이상 발생한다면 어떤 인덱스가 반환되는지 알 수 없다. 이는 반환된 인덱스로 검색하면 `val` 을 찾을 수 있다는 것을 의미한다.

→ 이는 동일한 입력에 대해 여러개의 유효한 출력을 허용한다.

**이것은 일반적인 의미에 `nondeterministic` 와는 다르다는 것을 알아야 한다.** 

- `**Nondeterministic code`는 동일한 입력을 가진 동일한 프로그램에서 호출되더라도 때로는 한가지 방식으로 동작할 수 있고, 때로는 다른 방식으로 동작 할 수 있다.**

**ex) 코드의 동작이 랜덤 숫자에 따라 달라지거나, 멀티 프로세스 환경에서 타이밍에 따라 달라질 때 발생**

**그러나 `deterministic spec`는 `nondeterministic implementation` 을 가질 필요가 없다.( `deterministic implementation`으로 만족 할 수 있기 때문에)**

`**underdeterminded` : not deterministic specification → 결정적이지 않은 스펙**

- ************underdetermined `find` 스펙은 `find(First)`와 `find(Last)` 으로 다시 만족되며, 각 구현들은 자체적인(fully deterministic) 방식으로 결정성이 부족한 것(`underdeterminedness`)을 해결한다.**
- `**find(OneOrMore, AnyIndex)` 스펙은 이런 경우에 어떤 인덱스가 반환되는지에 의존 할 수 없다.**

**→ 이러한 스펙은 `nondeterministic implementation` 을 가진 코드로 만족 시킬 수 있다.**

ex) 배열에 처음부터 검색할지 아니면 뒤에서부터 검색할지 결정하기 위해서 동전을 던지는 것

- **우리가 접 할 모든 경우에서, 스펙에서 결정성이 부족한 경우에는 구현자가 구현 시간에 결정한 선택을 제공한다.**

**즉, `underdetermined spec` 는 일반적으로 `fully-deterministic implementation` 으로 구현된다.**

---

## ****Declarative vs. operational specs****

**대략 스펙은 두가지 종류가 있음**

- **Operational specification** : 메서드가 수행하는 일련의 단계를 제공한다. `pseudocode description` 은 작동된다.

`**pseudocode description` : 의사 코드라고도 함. 알고리즘이 수행해야할 내용은 우리가 사용하는 언어로 간략하게 서술해 놓은 것**

- **Declarative specification : 중간 단계에 대한 자세한 내용은 나와있지 않는 대신 최종 결과의 속성과 초기 상태와의 관계를 알려준다.**

**→ `Declarative specification`이 좀 더 선호된다.**

**why?**

**더 짧고 이해하기 쉽다. 가장 중요한 점으로는 클라이언트가 의존할 수 있는 구현의 세부 정보를 실수로 노출하지 않기 때문이다.(또한 구현이 변경되면 더 이상 유지되지 않음)**

**ex) 만약 우리가 위에서 언급된 `find` 메서드의 두 구현중 하나만 사용하려고 할 때 스펙에서 “이 메서드는 `val` 을 찾을 때 까지 배열을 따라 내려간다” 고 말하고 싶지 않을 것이다. 왜냐하면 뜻이 모호하기도 하면서, 이 스펙은 인덱스가 낮은쪽에서 높은곳으로 진행되고 가장 낮은 인덱스를 반환할 것을 암시하므로 호출자가 이를 원하지 않을수도 있기 때문이다.**

**프로그래머들이 종종 operational specification에 빠지는 이유는 스펙 주석을 이용해서 관리자에게 구현에 대해 설명하기 때문이다. 그러기 위해서는 스펙 주석이 아닌 메서드 바디내에 설명을 해주는 것이 좋다.**

**ex) 특정 스펙에 대해 declarative 적으로 표현하는 방법은 여러가지가 있다.**

```java
static boolean startsWith(String str, String prefix)
 effects: returns true if and only if there exists String suffix
            such that prefix + suffix == str
------------------------------------------------------------------------------
static boolean startsWith(String str, String prefix)
 effects: returns true if and only if there exists integer i
            such that str.substring(0, i) == prefix
------------------------------------------------------------------------------
static boolean startsWith(String str, String prefix)
 effects: returns true if the first prefix.length() characters of str
            are the characters of prefix, false otherwise
```

**코드의 클라이언트와 관리자를 위해서 가장 좋은 스펙을 선택하는 것은 본인 나름이다.**

---

## Stronger vs. weaker specs

- 메서드의 구현 방식이나 스펙 자체를 변경하고 싶다고 가정해보자.
- 메소드의 현재 스펙에 의존하는 클라이언가 이미 존재한다.

→ 기존 스펙을 새로운 스펙으로 교체해도 안전한지 여부를 결정하기 위해서 두 스펙의 동작을 어떻게 비교할까?

**스펙 S2가 스펙 S1보다 강하거나 같을 때**

- **S2의 precondition은 S1의 precondition보다 약하거나 같다.**
- **S1의 precondition의 상태를 만족하는것에 대해서 S2의 postcondition은 S1보다 강하다.**

→ 이런 경우 S2를 만족하는 구현을 S1을 만족시킬 수 있으며, 프로그램에서 S1을 S2로 대체해도 상관이 없다.

이러한 두가지 규칙은 여러가지 아이디어를 구현한다.

- **Precondition이 약해질 수 있다고 생각할 수 있다 : 고객에게 요구가 약해진다고(precondition이 약해진다)해서 싫어 할 일은 없다.**
- **postcondition을 강화할 수 있다 : 더 많은 약속을 하는 것을 의미함(결과의 조건이 많아지니까)**

**ex) `find` 의 스펙**

```java
static int findExactlyOne(int[] a, int val)
  requires: val occurs exactly once in a
  effects:  returns index i such that a[i] = val
```

**대체 가능하다.(`precondition`이 약화됨 : 정확히 한번에서 최소 한번)**

```java
static int findOneOrMore,AnyIndex(int[] a, int val)
  requires: val occurs at least once in a
  effects:  returns index i such that a[i] = val
```

**`postcondition`을 강화하여 대체(낮은 인덱스를 return 함)**

```java
static int findOneOrMore,FirstIndex(int[] a, int val)
  requires: val occurs at least once in a
  effects:  returns lowest index i such that a[i] = val
```

**→ 이렇게 작성을 할 수도 있다.**

```java
static int findCanBeMissing(int[] a, int val)
  requires: nothing
  effects:  returns index i such that a[i] = val,
              or -1 if no such i
```

---

## ****Diagramming specifications****

**가능한 모든 Java 메소드의 공간을 매우 추상적으로 상상해보자.**

**이 공간의 각 지점은 메서드의 구현공간을 나타낸다.**


![Untitled](./image/kys1651/Untitled.png)

- **위에서 정의한 `find(First)`, `find(Last)`를 정의한다. spec이 아니라 실제 동작을 구현하는 메서드 본문인 implementation이다. 그래서 우리는 점으로 표시한다.**

![Untitled](./image/kys1651/Untitled%201.png)

- **Specification은 가능한 모든 구현들의 공간의 영역을 정의한다.**
- **지정된 구현들은 스펙에 따라 동작하며 precondition - postcondition 조건을 충족하거나(지역 내부) 그렇지 않다.(지역 외부)**
- `**find(First)` , `find(Last)`,`find(OneOrMore,AnyInder)` 은 모두 스펙에 의해 정이되므로 지역 내부에 있다.**

**우리는 클라이언트들이 이 공간을 들여다본다고 생각할 수 있다. 스펙은 방화벽 역할을 한다.**

- **구현자는 자유롭게 스펙 내부를 이동하면서 클라이언트를 당황시킬 염려 없이 코드를 변경할 수 있다.**

→ 구현자가 알고리즘의 성능, 코드의 명확성을 개선하는 등 리팩토링을 하거나 버그를 발견했을 때 접근 방식을 변경할 수 있도록 하기 위해서 매우 중요하다.

- **클라이언트들은 어떤 구현을 받을지 알 수 없다.**

→ 스펙을 잘 따르면서, 구현이 갑자기 중단될 것이라는 걱정없이 구현 방식을 변경할 수 있는 자유가 있어야한다.

**유사한 스펙은 어떻게 연관이 될까? 스펙 S1을 시작으로 새로운 스펙 S2를 만든다고 가정해보자.**

만약 S2가 S1보다 강하면 어떻게 다이어그램에 표시될까?

- **먼저 Postcondition을 강화한다. 만약 S2의 Postcondition이 S1보다 강하면 S2가 더 강한 스펙이다.**

**→ postcondition이 더 강해진다는것은 구현자에게 자유를 줄이고 출력값에 대한 요구사항이 더 많아진다는 것이다.  이전에 `find(OneOrMore,AnyImdex)` 는 단순히 index `i` 만을 리턴해서 만족했지만 스펙의 요구로 가장 낮은 인덱스 `i`를 리턴해야한다.**

**그러므로 이제는 `find(OneOrMore, AnyIndex)` 에는 있지만 `find(OneOrMore, FirstIndex)`밖에 구현이 있다.**

 **`find(OneOrMore,FirstIndex)`안에는 있고 `find(OneOrMore, AnyIndex)` 밖에는 구현이 있을 수 있을까?** 

**→ 아니다. 이러한 구현들은 모두 `find(OneOrMore, AnyIndex)`의 요구보다 보다 더 강력한 postcondition을 가지고 있다. ( 가장 낮은 인덱스를 뽑는 Postcondition이 추가되었기 때문에)**

- **precondition을 약화시키면 어떻게 될까? S2의 스펙이 더 강해진다 :**  구현에서는 이전에 스펙에서 제외되었던 새로운 입력을 처리해야한다.

**→ 이전에는 잘못된 입력 값이 들어왔다면 눈치 채지 못했지만 이제는 잘못된 행동이러는 걸 알 수 있음**

![Untitled](./image/kys1651/Untitled%202.png)

- **************************************우리는 S1보다 S2가 강할 때 다이어그램에서 더 작은 영역을 정의한다는 걸 알 수 있다.(더 약한 스펙은 더 큰 영역을 가진다.)**************************************
- **그림에서 `find(Last)`는 배열 arr 끝에서 반복하므로 `find(OneOrMore,FIrstIndex)`를 만족하지 않고 영역 밖에 있다.**
- **S1보다 강하지도 약하지도 않은 다른 스펙 S3이 중복(S1만 만족하거나, S3만 만족하거나, 둘 다 만족하는 구현이 존재하도록)되거나 분리될수도 있다.**

---

## ****Designing good specifications****

**어떻게 좋은 메서드를 만들까? 메서드를 설계한다는 것은 기본적으로 스펙을 작성한다는 것을 의미한다.**

**스펙의 형식 : 읽기 쉽고, 간결 명료하게 잘 구성되어야 한다.**

**그러나 스펙의 내용은 규정하기 어렵다. 완벽한 규칙은 없지만 유용한 지침이 있다.**

### ****The specification should be coherent (스펙은 일관적이여야한다.)****

스펙은 다양한 케이스가 많이 없어야 한다. 긴 인자 목록, 많이 중첩된 If문과 boolean flag는 문제를 만든다.

```java
static int sumFind(int[] a, int[] b, int val)
  effects: returns the sum of all indices in arrays a and b at which
             val appears
```

**이건 잘 설계 된 프로시져가 아니다.**

→ 실제로 관련 없는 작업을 수행하기 때문에 일관성이 없다.(두 개의 배열에서 찾고 인덱스를 합한다.)

→ 하나는 지수를 찾는 절차, 하나는 지수를 더하는 절차, 즉 두 개의 개별 절차를 사용하는 것이 좋을 것이다.

**********Code Review에서 본 예시**********

```java
public static int LONG_WORD_LENGTH = 5;
public static String longestWord;

/**
 * Update longestWord to be the longest element of words, and print
 * the number of elements with length > LONG_WORD_LENGTH to the console.
 * @param words list to search for long words
 */
public static void countLongWords(List<String> words)
```

**전역 변수를 사용하는 것과 리턴하는 것 대신 출력하는 것외에도 일관성이 없다.**

- **단어 수를 세고 가장 긴 단어를 찾는 두가지 작업을 수행한다.**
- **이 두가지 작업을 서로 다른 두 가지 방법으로 분리하면 보다 단순하고(`ETU`) 다른 맥락에서 보다 유용하게 사용할 수 있다.(`RFC`)**

### ****The results of a call should be informative****

**map에 값을 넣는 방법에 spec**

```java
static V put (Map<K,V> map, K key, V val)
  requires: val may be null, and map may contain null values
  effects:  inserts (key, val) into the mapping,
              overriding any existing mapping for key, and
              returns old value for key, unless none,
              in which case it returns null
```

- **precondition에서 null을 배제하지 않았으므로 map에 null을 저장할 수 있다.**
- **postcondition에서 없을 때를 위한 special return value로 null을 사용한다.**

**→ 즉, null이 반환된다면 실제로 Null에 바인딩된건지 키 값이 존재하지 않아서 null을 리턴한건지 알 수 없다.**

**null을 삽입하지 않았다는 것을 확실히 알기 전까지 반환값이 쓸모 없기 때문에 이것은 좋은 디자인이 아니다.**

### ****The specification should be strong enough****

**물론 스펙은 일반적인 경우에 클라이언트에게 충분히 강력한 보증을 제공해야 한다. 즉, 클라이언트의 기본 요구사항을 충족해야한다.**

**특별한 경우를 지정할 때는 특별히 주의를 기울여야한다. 그렇지 않다면 유용한 방법이 훼손되지 않도록 해야한다.**

**예를 들어, 클라이언트는 실제로 어떤 변형이 이루어지는지는 알 수 없기 때문에 잘못된 인자에 대해 예외를 넣지는것은 괜찮지만 임의로 변경하는 것은 의미가 없다.**

```java
static void addAll(List<T> list1, List<T> list2)
  effects: adds the elements of list2 to list1,
             unless it encounters a null element,
             at which point it throws a NullPointerException
```

**만약 `NullPointerException` 이 발생한다면, 클라이언트는 `list1`에 `list2` 의 어떤 요소가 추가 되었는지를 스스로 알아내야한다.**

### ****The specification should also be weak enough(****스펙은 또한 충분히 약해야 한다)

**파일을 여는 방법에 대한 스펙**

```java
static File open(String filename)
  effects: opens a file named filename
```

- ********************************************************************************************************************************************************이는 중요한 세부사항이 부족하기 때문에 좋지 않은 스펙이다. 파일을 읽거나 쓰기 위해 열었는지, 이미 존재하는지, 생성 되었는지 또한 파일 열기를 보장하는 방법이 없기 때문에 너무 강력한 스펙이다.********************************************************************************************************************************************************
- **실행되는 프로세스에서 권한이 없을수도, 파일 시스템에 문제가 있을 수 있다. 그러므로 스펙은 훨씬 약한 내용을 말해야 한다.( 파일 열기를 시도해서 성공하면 파일에 특정 속성이 있다는 것이다.)**

### ****The specification should use *abstract types* where possible**

- **우리는 앞에서 Java Collections section에서 `List`, `Set`과 같은 추상적인 개념과 `ArrayList` , `HashSet` 과 같은 특정 구현을 구별 할 수 있음을 보았었다.**
- **추상적으로 스펙을 작성한다면 클라이언트와 구현자 모두에게 자유를 준다.**

**→ 자바에서 `HashMap` , `FileReade` 과 같은 특정 구현 대신 `Map` , `Reader`을 사용하는 것과 같음** 

```java
static ArrayList<T> reverse(ArrayList<T> list)
  effects: returns a new list which is the reversal of list, i.e.
             newList[i] == list[n-i-1]
             for all 0 <= i < n, where n = list.size()
```

**→ 클라이언트가 매개변수로 ArrrayList를 넣도록 강제하고 리턴또한 ArrayList로 강제한다.**

**스펙은 ArrayList에 의존하지 않게 작동하므로 `List` 를 이용하는 것이 좀 더 스펙에 좋다.**

---

## ****Precondition or postcondition?****

- **또 다른 설계 문제로는 Precondition을 사용할 것인지 여부이며, 만약 그렇다면 진행하기 전 메소드 코드가 precondition을 충족했는지 확인을 해야하는지 여부이다.**
- **실제로 precondition의 가장 일반적인 용도는 속성을 확인하는 방법이 어렵거나, 비용이 많이들기 때문에 정확하게 속성을 요구하는 것이다.**

**위에서 언급한 것 처럼 사소한 precondition이 아닌 경우 클라이언트는 precondition이 위배되는 나쁜 상태에서 메소드를 호출하지 않도록 해야하기 때문에 불편을 겪는다.**

→ **그렇지 않으면 오류를 복구할 수 있는 방법이 없기때문에**

**그래서 메소드 호출자들은 Precondition을 좋아하지 않는다.**

**ex) 자바 API클래스는 인수가 부적절할 대 확인되지 않은 예외를 던지도록 postcondition으로 지정하는 경향이 있음**

**→ 이러한 방법을 사용하면 잘못된 인수를 입력한 호출자 코드에서 버그등을 쉽게 찾을 수 있다.**

**일반적으로, 잘못된 값이 입력된 지점에서 가능한 빨리 실패하는 것(fail fast)이 좋다.**

**때로는 메서드를 허용할 수 없을 정도로 느리게 만들지 않고 조건을  확인하는 것이 불가능하며, 이러한 경우에는 precondition이 필요한 경우가 많다.** 

**만약, 우리가 `find` 메소드를 이진 검색으로 사용하기 위해서는 일단 배열을 정렬해야한다.**

**배열이 정렬되었는지 실제로 확인 하도록 메서드를 강제하면 선형 시간이 아닌 로그로 결과를 얻는 이진 검색의 목적이 실패한다.**

- **precondition을 사용할지에 대한 결정은 공학적 판단이다.**
- **핵심 요소는 점검 비용(코드 작성 및 실행)과 방법의 범위이다.**
- **클래스내에서 로컬로만 호출되는 경우, 메소드를 호출하는 모든 곳을 확인하여 precondition을 만들 수 있다.**

→ 이 방법이 공개적이고 다른 개발자들도 사용한다면 precondition을 사용하는 것은 좋지 않다. 대신 자바 API처럼 예외를 던지는게 좋다.

---

## About access control