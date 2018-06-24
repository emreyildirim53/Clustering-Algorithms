### Fuzzy C Means Görsel Kümeleme Uygulaması

Örneklem uzayında veriyi temsil eden kordinatlar belirlenen küme sayısı parametresine göre cümeleme işlemi yapılmaktadır. Küme sayısı belirlenemediği durumlarda Elbow Tekniği ile yaklaşık bir küme sayısı tespit edilmeye çalışılmıştır. Elde edilen küme görüntüsü rgb (red/green/blue) formatında renklendirilmiştir. Kümeye aitlik derecesine göre her bir nokta renklendirilmiştir. 

#### Arayüz tasarımı

![](img/interface.png)

**File URL: (Open button after)** Dinamik olarak seçilen .csv uzantılı örneklem.<br>
**Choose Example:** Önceden tanımlı örneklem seçilebilmektedir.<br>
**Show Points:** Algoritma çalışmadan önce örneklemi gözlemlemek için hazırlanmış ekran.<br>
**Elbow:** Küme sayısının tespit edilmesi için kullanılan tekniğin uygulanma ekranı.<br>
**C Means On Points:** Uygulamanın çalıştırıldığı penceredir.<br>
** <- Before:** Algoritmanın bir önceki adımına geri dönmek için kullanılır. (Algoritmanın çalıştırılması gerekmektedir.)<br>
** After ->:** Algoritmanın bir sonraki adıma geri dönmek için kullanılır.(Algoritmanın çalıştırılması gerekmektedir.)<br>

#### Örneklem görüntüsü:

![](img/sample.png)



#### Örneklem Fuzzy C Means uygulama sonrası görüntüsü:

![](img/cmeans.png)

