# ****Abstraction Functions & Rep Invariants (23.10.05)****

### Objectives

- Invariants
- representation exposure
- abstraction functions
- representation Invariants

- 추상화 함수 그리고 Invariants(상수)의 개념을 통해 class에 ADT를 구현하는것이 무엇을 의미하는지에 대해 공부한다.
- 이러한 수학적 개념은 소프트웨어 디자인에 있어 매우 실용적이다.
- 추상함수는 데이터 유형에 따른 equality를 정의하는 방법을 제공한다.
- rep invariant는 데이터 구조로 인해 발생되는 버그를 더 쉽게 찾을 수 있게 제공한다.

## Invariants

- 좋은 ADT를 만드는것에 대한 논의에 대해서 가장 중요한 속성은 그 자체로 불변성을 보존하는 것이다.
- 불변성이란? → 프로그램의 런타임 상황에서 항상 True를 유지하는 상태
- 불변의 대상은 lifeTime 내내 같은 값을 유지해야한다.
- ADT가 고유 불변량을 보존한다는 것은 그에 대한 책임이 있다는 말입니다.
- ADT가 자신의 불변성을 보장할때 코드는 더 쉬워집니다. String 같은 불변 객체를 다룰 때에는 대게 String을 배제합니다.

### Immutability

- 첫번째 예시
    
    ```java
    /**
     * This immutable data type represents a tweet from Twitter.
     */
    public class Tweet {
    
        public String author;
        public String text;
        public Date timestamp;
    
        /**
         * Make a Tweet.
         * @param author    Twitter user who wrote the tweet.
         * @param text      text of the tweet
         * @param timestamp date/time when the tweet was sent
         */
        public Tweet(String author, String text, Date timestamp) {
            this.author = author;
            this.text = text;
            this.timestamp = timestamp;
        }
    }
    ```
    
- 어떻게 해당 `Tweet` 객체를 불변으로 할 수 있을까요?
- immutabiliy에 대한 첫 번째 위협으로 뽑자면 누군가가 해당 객체를 수정하는데 있습니다.
    
    ```java
    Tweet t = new Tweet("justinbieber",
                        "Thanks to all those beliebers out there inspiring me every day",
                        new Date());
    t.author = "rbmllr";
    ```
    
- 이런 클래스 변수를 노출하는 것은 **representaion exposure** 의 한 예이다.
- 이런 representation exposure는 오직 invariants 뿐만 아니라 representation independence까지 위협한다.
- 접근 제어자를 사용한 예
    
    ```java
    public class Tweet {
    
        private final String author;
        private final String text;
        private final Date timestamp;
    
        public Tweet(String author, String text, Date timestamp) {
            this.author = author;
            this.text = text;
            this.timestamp = timestamp;
        }
    
        /** @return Twitter user who wrote the tweet */
        public String getAuthor() {
            return author;
        }
    
        /** @return text of the tweet */
        public String getText() {
            return text;
        }
    
        /** @return date/time when the tweet was sent */
        public Date getTimestamp() {
            return timestamp;
        }
    
    }
    ```
    
- `public` `private` 같은 접근 제어자는 외부에서 접근 할 수 있는 필드를 나타낸다.
- 앞전에 배웠던 `final` 도 마찬가지.
- 그러나 여전히 노출 된곳은 존재한다…
    
    ```java
    /** @return a tweet that retweets t, one hour later*/
    public static Tweet retweetLater(Tweet t) {
        Date d = t.getTimestamp();
        d.setHours(d.getHours()+1);
        return new Tweet("rbmllr", t.getText(), d);
    }
    ```
    
- `retweetLater` 는 리트윗을 시도하는 코드입니다. ( 한시간 뒤에 )
- `d` 는 mutable 객체라는 것을 명심하자. → d를 수정시 t까지 수정함
- 해당 코드는 객체 Tweet에 대한 불변성이 깨진 상태
- 이건 앞에서 배운 defensive copying으로 조절 가능
    
    ```java
    public Date getTimestamp() {
        return new Date(Date.getTime());
    }
    ```
    
- `clone()`  은 가변객체를 복제하는 기능을 제공한다.
- defensive copying으로도 여전히 문제되는 부분이 존재한다.
    
    ```java
    /** @return a list of 24 inspiring tweets, one per hour today */
    public static List<Tweet> tweetEveryHourToday () {
        List<Tweet> list = new ArrayList<Tweet>();
        Date date = new Date();
        for (int i=0; i < 24; i++) {
            date.setHours(i);
            list.add(new Tweet("rbmllr", "keep it up! you can do it", date));
        }
        return list;
    }
    ```
    
- 이 코드는 24시간 마다 새로운 Date 객체를 만들어 매 시간마다 트윗한다.
- 하지만 이 코드는 모든 Tweet이 하나의 Date 객체를 참조한다…
- defensive copying을 활용해서 이를 해결
    
    ```java
    public Tweet(String author, String text, Date timestamp) {
        this.author = author;
        this.text = text;
        this.timestamp = new Date(timestamp.getTime());
    }
    ```
    
- 일반적으로 모든 인자 타입과 리턴 타입을 주의 깊게 검사해야한다.
- 유형 중에 하나라도 mutable 한 경우 직접 그 참조를 반환하지 않도록 주의한다… 노출이 생기지 않게 주의하자
- 그렇다면 이렇게 코드로 하지 말고 상세한 명세로 회피 할순 없는걸까?
    
    ```java
    /**
     * Make a Tweet.
     * @param author    Twitter user who wrote the tweet.
     * @param text      text of the tweet
     * @param timestamp date/time when the tweet was sent. Caller must never
     *                   mutate this Date object again!
     */
    public Tweet(String author, String text, Date timestamp) {
    ```
    
- 이러한 접근은 다른 대안이 없을때 주로 사용된다.
- 예를들어 가변 객체가 너무 클 경우가 있다.
- 하지만 이러한 방식은 프로그램을 추론하는데 들어가는 비용이 너무 큼을 명시하자.
- 불변성을 보장하는 것은 항상 가치가 높다는 것을 명심하자.
- 물론 처음부터 불변 유형을 고르는게 가장 합리적이다.

### ****Immutable Wrappers Around Mutable Data Types****

- 자바 collections는 immutable wrappers라는 걸 제공한다.
- `Collections.unmodifiableList()` 를 사용하면 일반적인 List로 보이지만 불변 개체로 wrapping한다.
- 하지만 여전히 runTime의 불변성은 보장되지만 Compile 시간에는 불변성이 보장 되지 못한다.

## Rep Invariant and Abstraction Function

- 추상 데이터에 대한 기초가 되는 이론을 더 자세히 공부
- 밑의 이론은 그 자체로 추상 유형의 설계와 구현에 도움을 준다.
- 추상 유형을 생각할 때 두 개체의 관계를 고려하는건 도움이 된다.
- rep values(표현 값 공간)은 실제 개체의 값으로 구성된다.
- rep values 값의 공간은 설계된 값으로 구성된다. → 추상 값 공간 (플라토닉 엔티티)
- 그들은 정신적 객체로써 실존하진 않는다. 그러나 추상적인 타입으로써 클라이언트가 보기 원하는 방식이다.
- 예를 들어 일반적인 경계가 없는 정수의 경우는 수학정 정수 전체가 추상 값의 공간(AF)이다.
- 하지만 경계가 있는 정수 배열은 이러한 정수의 경계는 중요하지 않다.
- 물론 추상 유형의 생성자는 실제 경계를 생각을 해야한다.
    
    ```java
    public class CharSet {
        private String s;
        ...
    }
    ```
    
    - 표현공간 R(문자열) 그리고 추상공간 A(수학적 문자 집합)이다.
    - R >- A 라고 볼 수 있다.
    - 몇가지 주의할 점
        - 모든 추상값은 어떤 참조 값에 의해 mapped된다.
        - 일분 추상 값은 둘 이상의 참조 값에 의해 mapped된다. {”abc”, “bac”} → {a, b, c}
        - 모든 참조 값이 mapped된건 아니다. {”aabbcc” → x } → 중복을 허용하지 않는다면…
            
- 우리는 몇개의 요소와 관게를 설명한다.
    - 참조 값을 추상값에 매핑하는 추상화 함수
        
        > $AF : R → A$
        > 
        - 이 관계가 일대일 대응이라고는 볼수 없지만… 종종 부분적이라고는 말할 수 있다.
    - 참조 값을 booleans에 매핑하는 rep invariant
        
        > $RI : R → boolean$
        > 
        - 참조 값 r 같은 경우 r이 AF에 의해서 매핑될 때만 RI(r)가 참이다.
        - 즉 주어진 참조 값이 잘 형성되었는지 여부를 알인다.
- rep invariant와 추상화 함수는 코드 옆에 문서화 되어야함을 기억하자
    
    ```java
    public class CharSet {
        private String s;
        // Rep invariant:
        //    s contains no repeated characters
        // Abstraction Function:
        //   represents the set of characters found in s
        ...
    }
    ```
    
- 추상 함수와 rep invariants의 일반적인 혼란은 그들이 참조와 추상 값의 공간에 대해서 결정된다는 것이다.
- 추상 값의 공간 만으로는 AF와 RI를 결정할 순 없다.
- 동일한 추상 타입에 대해 여러 표현 기능이 가능할 수 있다. 예를 들어 문자 집합은 문자열 또는 bit vector와 동일하게 표현될 수도 있다.
- 두 공간의 AF와 RI를 결정하지 않은 이유는 덜 명확할 수 있다.
- 표현 값 공간에 대한 유형을 정의하고 그것을 선택한다고 해서 그중 어떤 표현 값이 유요하다고 판단 될것이고 유효한 값중에는 어떻게 해석할지가 결정되지 않는다는 것이다.
- 위 처럼 문자열에 중복이 없다고 결정하는 대신 중복을 허용하지만 비 내림차순으로 나타난다고 요구하는것도 가능하다.
    
    ```java
    public class CharSet {
        private String s;
        // Rep invariant:
        //    s[0] <= s[1] <= ... <= s[s.length()-1]
        // Abstraction Function:
        //   represents the set of characters found in s
        ...
    }
    ```
    
- 각각의 가능성 있는 문자들은 서로 다른 추상함수로 매핑 될수 도 있다.
- 동일한 rep value를 가지더라도 다른 AF를 가진다.
- RI 가 모든 문자열을 가진다고 가정한다면 위에서처럼 AF를 정의해 배열 요소의 집합 요소로 해석 가능하다.
- $*AF(s)={[s1−s1],[s2−s2],...,[sn−sn]}*$
- 연속된 문자 쌍을 부분범위로 해석해 “acgg” sms {a-c}, {g-g}로 해석이 가능한 형태로 {a,b,c,g} 로 나타낼수 있다.
- $*RI(s)=true for any string s*$
- RI 가 같더라도 다양한 추상화함수가 아래와 같이 정의 가능하다.
    
    ```java
    public class CharSet {
        private String s;
        // Rep invariant:
        //    s.length is even
        //    s[0] <= s[1] <= ... <= s[s.length()-1]
        // Abstraction Function:
        //   represents the union of the ranges
        //   {s[i]...s[i+1]} for each adjacent pair of characters
        //   in s
        ...
    }
    ```
    
- 추상 타입을 설계한다는 것은 두 공간(추상 값 공간, 대표 값 공간)을 선택하는것 뿐만 아니라, 어떤 rep values를 결정하고 그거를 해석할지를 결정 한다는 것이다.

### Example: Rational Numbers

```java
public class RatNum {
    private final int numer;
    private final int denom;

    // Rep invariant:
    //   denom > 0
    //   numer/denom is in reduced form

    // Abstraction Function:
    //   represents the rational number numer / denom

    /** Make a new Ratnum == n. */
    public RatNum(int n) {
        numer = n;
        denom = 1;
        checkRep();
    }

    /**
     * Make a new RatNum == (n / d).
     * @param n numerator
     * @param d denominator
     * @throws ArithmeticException if d == 0
     */
    public RatNum(int n, int d) throws ArithmeticException {
        // reduce ratio to lowest terms
        int g = gcd(n, d);
        n = n / g;
        d = d / g;

        // make denominator positive
        if (d < 0) {
            numer = -n;
            denom = -d;
        } else {
            numer = n;
            denom = d;
        }
        checkRep();
    }
}
```

- RI 는 numerator/denominator가 감소된 상태, (2,4), (18,12) 같은 쌍은 RI 외부에 그린다.
- 더 많은 허용적인 RI로 동일한 ADT를 구현하는 것이 훨씬 합리적이다.

### Checking the Rep Invariant

- RI 는 단순하게 깔끔한 수학적 아이디어라곤 볼 수 없다.
- RI 불변성을 초기에 주장하는 경우 버그를 조기에 발견 할 수도 있다.
- RatNum의 RI를 테스트 하는 방법
    
    ```java
    // Check that the rep invariant is true
    // *** Warning: this does nothing unless you turn on assertion checking
    // by passing -enableassertions to Java
    private void checkRep() {
        assert denom > 0;
        assert gcd(numer, denom) == 1;
    }
    ```
    
- 전체적으로 `checkRep()` 을 호출하여 모든 생성과 변형 작업에서 RI 불변성을 주장해야한다.
- 위 코드는 각각의 constructors의 끝에 checkRep()을 호출하고 있다.
- Obersver methods는 일반적으로 `checkRep()` 을 호출할 필요는 없지만 이렇게 하는 방식은 좀더 defensive 한 방식입니다.
- 왜?  모든 방향에서 `checkRep()` 을 호출하는것은 당신이 RI를 조금더 잘 잡을수 있다는 것입니다.
- 왜 checkRep이 private인가? → RI를 체크하고 RI를 확인할 책임은 누가져야하는가? clients? 구현자?

### No Null Values in the Rep

- 이전 파트에서 읽었듯이 null values는 여러가지 문제를 일으킨다.
- null은 우리 공부에서 언제나 가장 제거해야하는 항목이다.
- 우리는 추상 데이터 타입에 또한  null 참조를 금지할것입니다.

```java
class CharSet {
    String s;
}
```

- `s` 는 당연히 null이 아닙니다!
- 하지만 `checkRep()` 을 구현 할때 여러분은 여전히 `s != null` 이라는걸 체크해야합니다.
- `s` 가 `null` 이라면 바로 실패해야하죠
- 자바에서 기본으로 제공해주는 s를 null 인지 알려주는 예시
    
    ```java
    private void checkRep() {
        assert s.length() % 2 == 0;
        ...
    }
    ```
    
    - assert가 있는데 이렇게 굳이 할 필요는 없지..
- assert `s != null` 매우 명시적이다.

## ****Documenting the AF, RI, and Safety from Rep Exposure****

- class 안의 RI 와 AF를 문서화하는 것을 매우 좋습니다.
- 또다른 논쟁은 **Rep Exposure argument safety** 입니다. 이는 rep의 각 부분을 조사하고 코드에에서 rep를 다루는 부분에서(특히 파라미터와 return값)을 살펴보고 rep가 노출하지 않은 이유를 서술
- `Tweet` 은 Invariant, abstraction function 안정성이 완전히 문서화된 예
    
    ```java
    // Immutable type representing a tweet.
    public class Tweet {
    
        private final String author;
        private final String text;
        private final Date timestamp;
    
        // Rep invariant:
        //   author is a Twitter username (a nonempty string of letters, digits, underscores)
        //   text.length <= 140
        // Abstraction Function:
        //   represents a tweet posted by author, with content text, at time timestamp
        // Safety from rep exposure:
        //   All fields are private;
        //   author and text are Strings, so are guaranteed immutable;
        //   timestamp is a mutable Date, so Tweet() constructor and getTimestamp()
        //        make defensive copies to avoid sharing the rep's Date object with clients.
    
        // Operations (specs and method bodies omitted to save space)
        public Tweet(String author, String text, Date timestamp) { ... }
        public String getAuthor() { ... }
        public String getText() { ... }
        public Date getTimestamp() { ... }
    }
    ```
    
- 우리는 어떠한 명시적인 불변 조건도 `timestamp` 에 가지고 있지 않다는 점을 명심하십시오. `timestamp != null` 그러나 여전히 `timestamp` 의 전체 유형의 불변성 속성은 변경되지 않은 immutability입니다.
- 왜냐하면 전체 내부 argument에서 이것을 변경할 것이 없기 떄문입니다.
- `RatNum` 의 argument
    
    ```java
    // Immutable type representing a rational number.
    public class RatNum {
        private final int numer;
        private final int denom;
    
        // Rep invariant:
        //   denom > 0
        //   numer/denom is in reduced form, i.e. gcd(|numer|,denom) = 1
        // Abstraction Function:
        //   represents the rational number numer / denom
        // Safety from rep exposure:
        //   All fields are private, and all types in the rep are immutable.
    
        // Operations (specs and method bodies omitted to save space)
        public RatNum(int n) { ... }
        public RatNum(int n, int d) throws ArithmeticException { ... }
        ...
    }
    ```
    

### How to Establish Invariants

- Invariant는 프로그램 전체에 대해서 True인 특성으로, 객체에 대한 invariant의 경우 객체 전체 수명을 감소시킵니다.
- Invariant를 유지하기 위해선 다음을 수행해야합니다.
    - 객체의 초기 상태를 invariant True로 만듭니다.
    - 객체의 모든 변경상항에 대해 invariant True로 만듭니다.
- ADT 연산자로 이를 변환하면 다음과 같은 의미를 가집니다.
    - creators와 producers는 새로운 객체 인스턴스에 대한 invariant를 설정해야합니다.
    - mutators 와 observers는 invariant를 보존해야합니다.
- rep exposure는 언제나 상황을 더 복잡하게 만듭니다. 만약 노출 된다면 ADT의 연산 뿐만 아니라 프로그램 어디서든지 객체가 변경되어질 수 있습니다.
- 이러한 현상은 invariant를 보장할 수 없어집니다.
- **Structural Induction** 추상 데이터 타입의 invariant가 다음과 같은 경우
    - creators와 producers에 의해 설립됨
    - mutators와 observers에 보존됨
    - rep exposure가 노출되지 않음

## ****ADT invariants replace preconditions****

- 지금까지 공부한것을 합쳐 잘 설계된 추상 타입은 규정해야할 전제 조건을 캡슐화 할 수 있다는 것이다.
- 예를 들어 spec이 다음과 같을때 정교한 precondition
    
    ```java
    /**
     * @param set1 is a sorted set of characters with no repeats
     * @param set2 is likewise
     * @return characters that appear in one set but not the other,
     *  in sorted order with no repeats
     */
    static String exclusiveOr(String set1, String set2);
    ```
    
- 이를 대체하는 ADT를 사용할 수 있다. (precondition을 대신함)
    
    ```java
    /** @return characters that appear in one set but not the other */
    static SortedSet<Character> exclusiveOr(SortedSet<Character>  set1, SortedSet<Character> set2);
    ```
    
- ADT의 이름 같은 경우 이 프로그램을 사용하는 모든 프로그래머가 사용하기에 이해가 더 쉽다.
- 또한 Static Checking이 가능하다.

## Summary

- invariant는 개체의 수명동안 ADt 개체가 항상 참인 것이다.
- 좋은 ADT는 그 자체에 대한 불변성을 보존한다.  → 버그로부터 안전해짐
- Rep exposure는 RI와 invariant preservation을 모두 보존한다.
- AF는 구체적인 값을 추상적인 값에 매핑해주는 함수이다.
- ADT를 활용해 이해하기 쉽고 변화할 준비가 된 코드를 작성 가능하다.
