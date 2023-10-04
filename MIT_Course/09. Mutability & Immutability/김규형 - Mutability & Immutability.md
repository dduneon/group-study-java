# Mutability & Immutability(23.09.26)

### Object

- 가변 객체와 가변성에 대해 이핸다.
- allasing(별명 짓기)를 Identify 성과 가변성의 위험을 이해
- 불변성을 사용해 정확성, 명확성,등을 개선한다.

## ****Mutability****

- Java의 일부 객체는 불변성을 가지고 있다.
- 한번 생성되면 → 같은 값을 가지고 있다. *[Immutability]*
- 객체의 값을 변경하는 메소드가 있다. → *[Mutability]*
- `String` 은 불변 타입의 가장 큰 예시이다. 같은 객체는 항상 동일한 문자열만을 나타낸다.
    
    ```java
    String s = "a";
    s = s.concat("b"); // s+="b" and s=s+"b" also mean the same thing
    ```
    
    - s 라는 String 객체는 언제나 “a” 만을 담고 있다.
    - 하지만 `concat` 을 통해 이어 붙일 시에 새로운 ab라는 객체를 만들어 저장한다.
    - s가 가르키는 주소가 변경되는 형태
        
        ![reassignment.png](img/kkh/reassignment.png)
        
- `StringBuilder` 는 가변 타입의 예시이다. 문자열을 일부 삭제, 삽입등을 방법을 가지고 있다.
    
    ```java
    StringBuilder sb = new StringBuilder("a");
    sb.append("b");
    ```
    
    - 하지만 `StringBuiler` 는 다르다.
    - 하나의 sb 객체에 값을 직접 수정하는 식으로 변경한다.
        
        ![mutation.png](img/kkh//mutation.png)
        

```java
String t = s;
t = t + "c";

StringBuilder tb = sb;
tb.append("c");
```

- 일반적인 `String` 에서 `t` 와 `s` 는 같은 객체를 가르킨다. 물론 `tb` 와 `sb` 또한 같은 객체를 가르킨다.
- 하지만 값의 변경이 이루어질 때 `t` 는 새로운 객체인 **abc** 를 생성해서 가르키지만, `tb` 는 `sb` 와 함께 가르키는 객체를 수정해 `sb` 또한 같이 값이 변경된다.
    
![string-vs-stringbuilder.png](./img/kkh/stringvsstringbuilder.png)
    
- `String` 의 사용이 일부 코드에서 지양되는 이유
    
    ```java
    String s = "";
    for (int i = 0; i < n; ++i) {
        s = s + n;
    }
    
    String s = "";
    for (int i = 0; i < n; ++i) {
    	  StringBuilder sb = new StringBuilder();
        for (int j = 0; j < s.length; j++) { // 기존 문자열 n -1 번 반복
    				sb.append(s.charAt(j);// 기존의 s의 문자열을 하나하나 새로운 값에 넣어야함 
    		}
    		sb.append(n);
    		s = sb;
    }
    ```
    
    - `String` 에 대한 많은 변경이 이루어질 때 너무나 많은 임시적 복사본이 생성된다.
    - 위 코드에서는 `n` 개의 `String` 객체가 생성될 위험을 가진다.
    - 시간 복잡도 면에서도  O($n^2$)를 가진다
- `StringBuilder` 객체는 이러한 문제를 해결 할 수 있다.
    
    ```java
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < n; ++i) {
      sb.append(String.valueOf(n));
    }
    String s = sb.toString();
    ```
    
    - `StringBuilder` 는 이러한 복사본을 최소화한다.
    - 마지막 까지 복사본을 만들지 않고 계속해서 객체 내부의 값만 수정한다.
    - 가변 객체는 객체를 공유함으로써 더 성능 좋은 통신이 가능하다.

## Risks of mutation

- 가변 타입을 불변 타입보다 강력하게 보인다.
- 그러면 왜 불변 타입을 사용하는걸까…?
- 불변 타입은 버그로부터 안전하고, 이해하기 쉬우며, 변화에 준비됬다.
- 가변성은 코드를 이해하기 어렵게 만든다 두가지 예시

### ****Risky example #1: passing mutable values****

```java
/** @return the sum of the numbers in the list */
public static int sum(List<Integer> list) {
    int sum = 0;
    for (int x : list)
        sum += x;
    return sum;
}
```

- 단순히 리스트의 값을 모두 더하는 코드
- 만약 절대 값도 처리하고 싶다면 다음과 같이 코드를 변경한다.

```java
/** @return the sum of the absolute values of the numbers in the list */
public static int sumAbsolute(List<Integer> list) {
    // let's reuse sum(), because DRY, so first we take absolute values
    for (int i = 0; i < list.size(); ++i)
        list.set(i, Math.abs(list.get(i)));
    return sum(list);
}
```

- 이 코드는 list를 직접 변경하여 사용한다.
- 얼핏 보면 효율적인 코드로 보인다. (DRY 우수)
- 하지만 이 코드는 잠재적인 버그를 가지고 있다. → 가변객체를 통과시키는것 , 추후에 디버깅 난이도 상승

```java
/** @return the sum of the absolute values of the numbers in the new list */
public static int sumAbsolute(List<Integer> list) {
    List<Integer> absoluteValues = new ArrayList<>();
    for (int num : list) {
        absoluteValues.add(Math.abs(num));
    }
    return sum(absoluteValues);
}
```

- 가변 객체를 넘기는것보단 새로운 리스트를 생성해서 넘기는 것이 조금더 안전하다.

### ****Risky example #2: returning mutable values****

- 우리가 가변 객체를 함수에 전달하면 생기는 문제를 보았다. 가변 객체를 return 하는 것은?
- `Date` 를 고려해보자. 이건 가변 타입이다. 우리가 만약 첫번 째 날이 봄날인지 가정하는 코드를 쓴다면?

```java
/** @return the first day of spring this year */
public static Date startOfSpring() {
    return askGroundhog();
}
```

- Groundhog 알고리즘을 사용하여 봄의 시작을 계산하는 코드

```java
// somewhere else in the code...
public static void partyPlanning() {
    Date partyDate = startOfSpring();
    // ...
}
```

- 전체적인 코드는 작동하지만 두가지 문제가 발생했다.
    - 매번 코드에 대한 질문을 대답해야 하기에 추후에 대답을 위해서 코드를 수정한다.
        
        ```java
        /** @return the first day of spring this year */
        public static Date startOfSpring() {
            if (groundhogAnswer == null) groundhogAnswer = askGroundhog();
            return groundhogAnswer;
        }
        private static Date groundhogAnswer = null;
        ```
        
    - 또한 봄의 날씨가 너무 춥가도 하기에 봄의 시작일에서 한달 뒤로 코드를 수정해야한다.
        
        ```java
        // somewhere else in the code...
        public static void partyPlanning() {
            // let's have a party one month after spring starts!
            Date partyDate = startOfSpring();
            partyDate.setMonth(partyDate.getMonth() + 1);
            // ... uh-oh. what just happened?
        }
        ```
        
    - partyDate의 `Month` 를 1 올렸지만, groundHogAnswer도 1 오르는 문제가 발생한다.
    - 또한 11(12월)의 경우 잘못된 값을 내보낼 수 있다
- 결과적으로 이 코드는…
    - 잠재적 버그를 가지고 있다.
    - 변화를 위한 준비는 좋지만 이것이 버그를 도입하지 않고 바꾸는것이 중요하다…

- 각 두 예제에서 `List<Integer>` 와 `Date` 를 불변 유형이였다면 이러한 문제를 피할 수 있을것이다.
- 물론 `Java.time` , `LocalDateTime` 같은 불변 타입을 사용한다면 버그를 피할 수 있다.
- 이런 예제는 왜 가변 타입이 나쁜 성능을 보이는지 알려줍니다.
- 이런 버그를 피하는 간단한 방법은 명세나 메소드 시그니처의 변경을 피하는 것이다
    - `startOfSpring()` 에서 항상 복사본을 return 한다.
    - `return new Date(groundhogAnswer.getTime());`
- 이러한 패턴을 **defensive copying** 이라고 한다.
- 이는 추상적인 데이터 타입이며 기존의 값에 영향을 주지 않고 자유롷게 사용이 가능하다.
- 그러나 이러한 방어적 복사는 이용자가 추가적인 공간과 작업이 요구 된다. 즉 99%의 사용자가 날짜를 변경하지 않더라도 이런 오버헤드는 발생한다.
- 불변성은 이런 오버헤드가 없다. 즉 이런 상황에서는 불변성이 더욱 이득이 되는 상황

## ****Aliasing is what makes mutable types risky****

- 일반적으로 가변 객체를 지역적으로 사용하는 것은 괜찮다.
- 하지만 다양한 객체에 **aliases** 라고 불리는 여러개의 참조를 가지는것에 문제가 있다.
    - `List` 객체를 사용하는 `sum` , `sumAbsolute` 이건 다양한 사람이 협업하는 과정에서 문제가 발생
    - `Date` 같은 경우 `groundhogAnswer` 와 `partyDate` 를 가리키는데 이 또한 각자 같은 객체를 참조하지만 다른 곳에 위치 되어 있어 코드를 읽기 어렵게 한다.
- 스냅샷 다이어그램을 통해 이러한 문제를 쉽게 머리에 이해할 수 있다.

## ****Specifications for mutating methods****

- 가변성 있는 메소드의 수행에 대해 명세에 포함하는 것은 중요합니다.
- 우리는 앞서 가변성이 버그의 원인이 됨을 확인하였다.

```
static void sort(List<String> lst)
requires: nothing
effects:  puts lst in sorted order, i.e. lst[i] <= lst[j]
              for all 0 <= i < j < lst.size()
```

- 가변성을 제거한 명세

```
static List<String> toLowerCase(List<String> lst)
requires: nothing
effects:  returns a new list t where t[i] = lst[i].toLowerCase()
```

- 추후 암묵적으로 불변이라고 이후 허용한다.
- 변이는 결국 끔찍한 버그로 이어지는 것을 언제나 명심할것

## ****Iterating over arrays and lists****

- 가변 객체중에 **Iterator** 를 확인해보자.
- Collections의 요소들을 순차적으로 확인하는 객체로써 자바에서는 List의 loop에서 주로 사용된다.

```java
List<String> lst = ...;
for (String str : lst) {
    System.out.println(str);
}
```

- 이걸 컴파일러는 이렇게 해석한다…

```java
List<String> lst = ...;
Iterator iter = lst.iterator();while (iter.hasNext()) {String str = iter.next();
    System.out.println(str);
}
```

- iterator가 가지고 있는 두가지 메소드
    - `next()` returns the next element in the collection
    - `hasNext()` tests whether the iterator has reached the end of the collection.
- 주목할 것은 `next()` 는 가변 메소드로써 단순히 요소를 리턴할 뿐만 아니라 다음 값을 찾는다

### MyIterator

- MyIterator의 구현
    
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
    
- **Instance varibles** : 자바에서 필드라고 불리는 인스턴스 변수로써 매개변수와 로컬 변수와는 차이가 있다. 객체의 인스턴스에 저장되고 일반적인 로컬 변수보다는 더 오래 가는것을 명심
- **Constructor** : 객체를 생성
- **this** : 인스턴스 변수를 참조하는데 사용
- etc…
- `list` 는 `final` 로 선언되어져 있다.
    - 이는 불변 객체로 선언되어 관리됨을 알 수 있다…
    - 이런 일반적인 반복자의 개념은 다양한 언어에서 사용된다. (디자인 패턴)

## ****Mutation undermines an iterator****

- 아래 코드는 iterator를 사용하는 예 이다.
    
    ```java
    /**
     * Drop all subjects that are from Course 6.
     * Modifies subjects list by removing subjects that start with "6."
     *
     * @param subjects list of MIT subject numbers
     */
    public static void dropCourse6(ArrayList<String> subjects)
    ```
    
- `dropCourse6` 은 클라이언트에게 인수가 변경될 것을 경고한다.
- 테스트를 위해서 입력을 분할한다.
    
    ```sql
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
    
- 테스트 결과
    
    ```bash
    // dropCourse6(["6.045", "6.005", "6.813"])
    //   expected [], actual ["6.005"]
    ```
    
    - 마지막은 실패했다.
- 왜 틀린 답이 나왔는지는 스냅샷 다이어그램을 활용한다면 편하다.

- 이건 모든 `Iterator` 를 사용하는 모든 곳에서 나타나는 문제입니다.

```java
for (String subject : subjects) {
    if (subject.startsWith("6.")) {
        subjects.remove(subject);
    }
}
```

- 이건 결과적으로 `[Concurrent­Modification­Exception](http://docs.oracle.com/javase/8/docs/api/?java/util/ConcurrentModificationException.html)`를 유발한다.
- 이 문제를 해결할려면…
    
    ```java
    Iterator iter = subjects.iterator();
    while (iter.hasNext()) {
        String subject = iter.next();
        if (subject.startsWith("6.")) {
    			iter.remove(subject);
        }
    }
    ```
    
    - `iter.remove()` 로 변경한다.
    - 기존의 `subjects.remove()` 는 리스트에서 해당 요소를 다시 찾아서 제거 한다. (예외발생)
    - `iter.remove()` 는 현재 순회 중에 요소를 삭제 할 수 있는 유일한 방법…
- 이렇게 해서 전체 문제를 해결했다고는 볼 수 없다.
    - 만약 같은 Iterator를 다른 프로그램이 실행중이라면..?’
- 이 문제에 대한 스냅샷 다이어그램
    
    ![스크린샷 2023-09-26 오후 4.04.33.png](img/kkh/img1.png)
    

## ****Mutation and contracts****

### ****Mutable objects can make simple contracts very complex****

- 가변 데이터 구조체에 대한 기능적 이슈입니다.
- 같은 가변 객체에 대한 다양한 참조(**aliasese** 라고도 불림)
- 다양한 위치에 있는 참조가 한 객체를 의존하고 있는 상태
- 이러한 경우 다양한 곳에 위치한 참조자 들끼리 서로 가변 객체를 좋은 방식으로 다루기를 기도하는 방법뿐…
- 이러한 방식의 대표적은 Java의 `Collections`
    - `Collections` 은 이러한 방식에 대해서 어떻게 기록하고 있는가?
    - 수정 (`List` 를 추가하거나 삭제)할때 어떤 식으로 책임은 `Collections` , `List` , `Iterator`  중에 누가 책임지나?
- Solution
    - **synchronizing** 사용 → *Concurrency* 보장
        
         `List list = Collections.synchronizedList(new ArrayList(...));`
        
    - *Stram()* ,  *parallelStream()* 활용
        
        ```java
        List<String> list = Arrays.asList("one", "two", "three", "four", "five");
        list.parallelStream().forEach(element -> {
        // 동시성으로 요소 처리
        });
        ```
        
- 이와 같이 가변 속성은 추론에 대한 비용이 들어가기 때문에, 프로그램과 데이터 구조에 대한 이해가 더 어렵다.
- 가변성은 편의성과 성능에 큰 이점을 주기에 버그 안정성과 비교해서 잘 비교해 사용하자.

### ****Mutable objects reduce changeability****

- 가변 객체는 구현자와 사용자 사이에서 높은 복잡한 제약을 요구하고 변경에 대한 높은 비용을 요구합니다.
- 즉 *Object* 를 사용한다는 것은 코드의 변경을 힘들게 하는 것이라고도 말할 수 있다.
- Example (MIT에서 사용자의 이름을 검색하고 9자리 식별번호를 반환)
    
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
    
    - 해당 명세에 따라 클라이언트가 이렇게 요청한다.
    
    ```java
    char[] id = getMitId("bitdiddle");
    System.out.println(id);
    ```
    
    - 이 상태에서 유저의 개인정보 보호를 위해 구현자와 사용자는 id 앞 5글자에 *****로 가리기로 한다.
    
    ```java
    char[] id = getMitId("bitdiddle");
    for (int i = 0; i < 5; ++i) {
        id[i] = '*';
    }
    System.out.println(id);
    ```
    
    - 구현자는 캐쉬를 사용한다.
    
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
    
    - 이러한 방식에 대해서 발생하는 문제가 있다.
    - `id` 는 가변 객체(aliased)에 * 를 추가한다면 cache의 배열까지 수정한다.
    - 가변 객체를 공유 하는 것은 또다시 누구의 책임이 있는지 애매해진다.
        1. 구현자는 return 값을 고정해야만 하는가?
        2. 사용자는 return 값을 수정하면 안되는가?
- 새로 적은 명세
    
    ```java
    public static char[] getMitId(String username) throws NoSuchUserException 
      requires: nothing
      effects: returns an array containing the 9-digit MIT identifier of username,
                 or throws NoSuchUserException if nobody with username is in MIT’s
                 database. Caller may never modify the returned array.
    ```
    
    - 여전히 문제가 존재한다.
    - 이 코드의 문제는 이러한 제약이 프로그램 전체에 미친다는 것이다.
        - array를 절대 수정하지 말것
    - 기존의 **precondition**과 **postcondition**들은 메소드의 주변만 생각했지 다른 곳에서까지 생각할 필요가 없었다는 것을 떠올리면 매우 큰 제약.
- 비슷한 문제를 가진 예제
    
    ```java
    public static char[] getMitId(String username) throws NoSuchUserException 
      requires: nothing
      effects: returns a new array containing the 9-digit MIT identifier of username,
                 or throws NoSuchUserException if nobody with username is in MIT’s
                 database.
    ```
    
    - 새로운 배열을 return 한다고 해서 완전한 문제를 해결할 수 있는것은 아니다.
    - 여전히 구현자는 새로운 array를 alias 하지 않는다고 보장하지 못한다.
- 더 나은 예제
    
    ```java
    public static String getMitId(String username) throws NoSuchUserException 
      requires: nothing
      effects: returns the 9-digit MIT identifier of username, or throws
                 NoSuchUserException if nobody with username is in MIT’s database.
    ```
    
    - `String` 을 return 함으로써 불변성을 보장한다.
    - 또한 불변성이 보장 받았기에 cache를 자유롭게 사용할 수 있다.  → 성능 향상

## ****Useful immutable types****

- 일반적으로 immutable은 Java API에서 문제를 회피 할 수 있다.
    - primitive type, primitive는 immutable 입니다.
    - `BigInteger` , `BigDecimal` 또한 immutable 입니다.
    - mutable한 `Date` 의 사용을 지양하세요. 시간을 기록할 때 에는 `java.time` 을 사용하세요.
    - 자바 Collections의 구현에 있어서 모두 mutable 입니다. immutable 하게 할려면 아래를 활용하세요
        - `Collections.unmodifiableList`
        - `Collections.unmodifiableSet`
        - `Collections.unmodifiableMap`
    - `[Unsupported­Operation­Exception](http://docs.oracle.com/javase/8/docs/api/?java/lang/UnsupportedOperationException.html)` 를 활용해서 `add` , `put` 등 mutations를 방지하세요
    - mutable한 collection을 다른 프로그램에 전달하기 전에 이 collection을 수정할수 없도록 wrapper 할 수 있습니다.
    - `final` 참조는 Object에서는 수정이 가능합니다.
    - `Collections` 는 `Collections.emptyList` 를 통해서 빈 불변 리스트를 생성할 수 있습니다.
        - 이 빈 리스트는 `null` 을 사용하는 것을 줄일 수 있는 장점이 있습니다.
        

## Summary

- mutability는 성능과 편의성에 우위를 가지지만, 버그의 위험성을 가진다. 또한 정확성을 추론하기에 높은 비용을 요구한다.
- immutable 객체의 차이에 대해서 `Strin` 은 immutable 객체, `final` 은 불변 참조를 이해한다.
- immutablility는 결과적으로 버그로부터 안전하고 이해하기 쉬우며, 변화에 준비된 것을 확인 할 수 있다.
    - 물론 비용에 대한 단점도 존재한다…
