# Interface (23. 10. 11)

### Objectives

- ADT를 정의하고 인터페이스를 구현하는 클래스를 작성하기

## Interfaces

- 자바의 `interface` 는 추상 데이터 유형 표현해 유용한 언어 메커니즘
- 메서드 시그니쳐는 있지만… bodies는 없다.
- `interface` 는 모든 메서드에 대한 메서드 bodies를 정의한다. 따라서 JAVA에서 인터페이스는 추상 데이터 타입을 정의하는데 한가지 방법으로 사용된다.
- 이러한 방식을 사용하는 장점은 사용자에게 contract를 명시하고 더이상은 아니라는 것이다.
- 사용자가 ADT를 이해하는 것 오직 그뿐
- `interface` 에는 인스턴스 변수를 넣을 수가 없기에 의도치 않은 종속성이 생성될 위험이 없음
- 구현은 서로 다른 클래스에서 분리된다.
- Interface는 여러 클래스와 프로그램에서 동시에 존재가 가능하다.
- 하나의 클래스로만 추상 데이터 타입을 표시한다면 더 어려워진다. → 일반적인 클래스는 두 표현을 동시에 적을 수 없다.
- 자바의 Static checking은 ADT에 대해서 컴파일러가 여러 실수를 잡을 수 있게도 도와준다.

## ****Example: MyString****

- `MyString` 의 예제를 다시 보자
    
    ```java
    /** MyString represents an immutable sequence of characters. */
    public interface MyString {
    
        // We'll skip this creator operation for now
        // /** @param b a boolean value
        //  *  @return string representation of b, either "true" or "false" */
        // public static MyString valueOf(boolean b) { ... }
    
        /** @return number of characters in this string */
        public int length();
    
        /** @param i character position (requires 0 <= i < string length)
         *  @return character at position i */
        public char charAt(int i);
    
        /** Get the substring between start (inclusive) and end (exclusive).
         *  @param start starting index
         *  @param end ending index.  Requires 0 <= start <= end <= string length.
         *  @return string consisting of charAt(start)...charAt(end-1) */
        public MyString substring(int start, int end);
    }
    ```
    
- Constructort를 사용하서 아래의 첫번째 구현
    
    ```java
    public class SimpleMyString implements MyString {
    
        private char[] a;
    
        /* Create an uninitialized SimpleMyString. */
        private SimpleMyString() {}
    
        /** Create a string representation of b, either "true" or "false".
         *  @param b a boolean value */
        public SimpleMyString(boolean b) {
            a = b ? new char[] { 't', 'r', 'u', 'e' }
                  : new char[] { 'f', 'a', 'l', 's', 'e' };
        }
    
        @Override public int length() { return a.length; }
    
        @Override public char charAt(int i) { return a[i]; }
    
        @Override public MyString substring(int start, int end) {
            SimpleMyString that = new SimpleMyString();
            that.a = new char[end - start];
            System.arraycopy(this.a, start, that.a, 0, end - start);
            return that;
        }
    }
    ```
    
- 최적화 구현
    
    ```java
    public class FastMyString implements MyString {
    
        private char[] a;
        private int start;
        private int end;
    
        /* Create an uninitialized FastMyString. */
        private FastMyString() {}
    
        /** Create a string representation of b, either "true" or "false".
         *  @param b a boolean value */
        public FastMyString(boolean b) {
            a = b ? new char[] { 't', 'r', 'u', 'e' }
                  : new char[] { 'f', 'a', 'l', 's', 'e' };
            start = 0;
            end = a.length;
        }
    
        @Override public int length() { return end - start; }
    
        @Override public char charAt(int i) { return a[start + i]; }
    
        @Override public MyString substring(int start, int end) {
            FastMyString that = new FastMyString();
            that.a = this.a;
            that.start = this.start + start;
            that.end = this.start + end;
            return that;
        }
    }
    ```
    
- 이전의 ADT와 비교한다면 static `valueOf` 가 현재 생성자 안에서 나타나며 `this` 를 사용하고 있다.
- 또한 `@Override` 의 표현의 사용 해당 annotation은 컴파일러에게 인터페이스의 메소드 중 하나와 동일한 시그니쳐를 가짐을 알린다.
- 물론 코드의 이해를 돕기 위해서도 사용된다.
- 우리가 새로 만든 `substring(...)` 에 집중하자면 이건 데이터의
- 빈 생성자의 경우 자바가 기본적으로 명시해준다.
- 하지만 **boolean b를** 받는 생성자를 추가함으로써 기본 생성자 또한 적어줘야한다.
- 좋은 ADT는 자신의 불변량을 보존한다.
- 아무것도 하지 않은 생성자는 좋지 않은 패턴이다. → 불변량을 설정하지 않으니깐…
- 사용자가 해당 ADT를 사용한다면..?
    
    ```java
    MyString s = new FastMyString(true); 
    System.out.println("The first character is: " + s.charAt(0));
    ```
    
- 아래 코드와 많이 유사하다.
    
    ```java
    List<String> s = new ArrayList<String>();
    ...
    ```
    
- 이 코드는 오히려 추상화 장벽을 무너트린다… 왜?
- Client가 구체적인 class 이름을 알아야함
- Java의 인터페이스는 생성자를 포함할 수 없기 때문에 실제로 생성자는 관련된 클래스 중에서 하나를 직접 호출해야하지만 그 생성자의 spec은 인터페이스에 적어져 있지 않음. → 동일한 생성자가 적용된다는 보장이 없음
- Java 8 이상에서는 static methods가 포함되도록 허용되어 Creator 연산을 흉내낼 수 있다.
- `valueOf` 방식으로 static factory method 추가한 코드
    
    ```java
    public interface MyString {
    
        /** @param b a boolean value
         *  @return string representation of b, either "true" or "false" */
        public static MyString valueOf(boolean b) {
            return new FastMyString(true);
        }
    
        // ...
    ```
    
    - 추상화 장벽을 허물지 않고 ADT를 사용
    
    ```java
    MyString s = MyString.valueOf(true);
    System.out.println("The first character is: " + s.charAt(0));
    ```
    

## Example : `Set`

- Java Collection 제공하는 interface와 구현을 분리하는 예
- `Set` 인터페이스
    
    ```java
    /** A mutable set.
     *  @param <E> type of elements in the set */
    public interface Set<E> {
    ```
    
- `Set` 은 generic 타입이다. → 타입은 추후에 결정됨
    
    ```java
        // example creator operation
        /** Make an empty set.
         *  @param <E> type of elements in the set
         *  @return a new set instance, initially empty */
        public static <E> Set<E> make() { ... }
    ```
    
- `make`  는 static factory 방식으로 구현됩니다.
- `Set<String> strings = Set.make();`
- 그리고 컴파일러는 새로운 `Set` 과 `String` object를 만드는 것으로 이해합니다.
    
    ```java
    // example observer operations
    
        /** Get size of the set.
         *  @return the number of elements in this set */
        public int size();
    
        /** Test for membership.
         *  @param e an element
         *  @return true iff this set contains e */
        public boolean contains(E e);
    ```
    
- 다음으로 두개의 observer 메서드 가 있다.
- 집합(set)에 대한 추상적인 개념으로 볼때 spec이 어떤지 .
- 특정 private field가 있는 집합의 특정 구현에 새부적인 구현을 언급하는것은 옳지 않다.
    
    ```java
     // example mutator operations
    
        /** Modifies this set by adding e to the set.
         *  @param e element to add */
        public void add(E e);
    
        /** Modifies this set by removing e, if found.
         *  If e is not found in the set, has no effect.
         *  @param e element to remove */
        public void remove(E e);
    ```
    

## ****Why Interfaces?****

- 왜 인터페이스를 사용하는가?
- **컴파일러와 인간 모두를 위한 문서** : 인터페이스는 컴파일러가 버그를 static Checking 하는 것 뿐만 아니라 다른 사람이 코드를 읽기 쉽게 한다.
- **성능 절충 허용** : 지역 독립적인 코드 사용의 제공
- **선택적인 메소드** : mutable / immutable 를 선택적으로 제공
- **과소 결정된 메서드** : ADT는 요소 순서를 지정하지 않은채로 정렬, 비 정렬로 변환을 제공한다.
- **클래스의 다양한 시각** : 자바 클래스는 다양한 메서드로 구현 될 수 있습니다. 하나의 인터페이스로 구현된 클래스로 서로 다른 UI를 제공가능하다. (위젯과 위젯 목록 UI)
- **신뢰성이 떨어지는 구현** : 인터페이스는 버그를 포함할 수 있습니다. 그렇기에 계속해서 더 우수한 인터페이스를 구현해 버그가 발생하는 가능성을 낮출 수 있다.

## ****Realizing ADT Concepts in Java****

| ADT concept | Ways to do it in Java | Examples |
| --- | --- | --- |
| Abstract data type | Single class | http://docs.oracle.com/javase/8/docs/api/java/lang/String.html |
|  | Interface + class(es) | http://docs.oracle.com/javase/8/docs/api/java/util/List.html and http://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html |
| Creator operation | Constructor | http://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html#ArrayList-- |
|  | Static (factory) method | http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#singletonList-T-, http://docs.oracle.com/javase/8/docs/api/java/util/Arrays.html#asList-T...- |
|  | Constant | http://docs.oracle.com/javase/8/docs/api/java/math/BigInteger.html#ZERO |
| Observer operation | Instance method | http://docs.oracle.com/javase/8/docs/api/java/util/List.html#get-int- |
|  | Static method | http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#max-java.util.Collection- |
| Producer operation | Instance method | http://docs.oracle.com/javase/8/docs/api/java/lang/String.html#trim-- |
|  | Static method | http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#unmodifiableList-java.util.List- |
| Mutator operation | Instance method | http://docs.oracle.com/javase/8/docs/api/java/util/List.html#add-E- |
|  | Static method | http://docs.oracle.com/javase/8/docs/api/java/util/Collections.html#copy-java.util.List-java.util.List- |
| Representation | private fields |  |

## Summary

- 자바 인터페이스는 추상적인 데이터 타입을 연산을 지원하는 집합으로 공식화 하는데 도움을 준다.
