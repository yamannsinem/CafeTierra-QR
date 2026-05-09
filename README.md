# tierraCafe-QR
QR-based cafe menu and service management system.

# tierra A LA MESA - Dijital Menü ve Kafe Yönetim Sistemi

**tierra**, modern kafe işletmeleri için tasarlanmış; müşterilerin masalarındaki QR kodlar üzerinden sipariş verebildiği, yöneticilerin ise tüm operasyonu (sipariş, menü, masa ve garson çağrıları) tek bir panelden kontrol edebildiği kapsamlı bir **Jakarta EE** tabanlı web platformudur.

---

## 🚀 Teknolojik Yığın (Tech Stack)

### 🔹 Backend (Sunucu Tarafı)
* **Java 17 & Jakarta EE 10:** Kurumsal düzeyde, ölçeklenebilir ve sağlam bir altyapı.
* **EJB (Enterprise JavaBeans) 4.0:** İş mantığının yönetimi ve güvenli transaction kontrolü (Stateless Beans).
* **JPA (Jakarta Persistence API) & Hibernate:** Veritabanı etkileşimi ve ORM süreçleri.
* **PostgreSQL:** Verilerin güvenli ve performanslı bir şekilde saklanması için kullanılan ilişkisel veritabanı.
* **Bean Validation:** Veri doğruluğunu sağlamak için kullanılan sunucu taraflı kontroller.

### 🔹 Frontend (Arayüz Tarafı)
* **JSF (Jakarta Server Faces) 4.0:** Dinamik ve bileşen tabanlı kullanıcı arayüzü motoru.
* **AJAX (`f:ajax`):** Sayfa yenilemeden sepet güncelleme ve canlı sipariş takibi sağlayan akıcı kullanıcı deneyimi.
* **Bootstrap 5:** Mobil uyumlu (Responsive) tasarım altyapısı.
* **CSS3 & Glassmorphism:** Modern, estetik ve koyu tema (Dark Mode) üzerine kurulu görsel dil.

---

## 🏗️ Mimari Yapı

Proje, sorumlulukların net bir şekilde ayrıldığı **Layered Architecture (Katmanlı Mimari)** ve **Facade Design Pattern** üzerine inşa edilmiştir:

1.  **Entity Katmanı:** Veritabanı tablolarını temsil eden POJO sınıfları (`CafeOrder`, `MenuItem`, `CafeTable`, `Category`, `WaiterRequest`).
2.  **Facade Katmanı (EJB):** Veritabanı işlemlerini ve karmaşık iş kurallarını soyutlayan katman.
3.  **Managed Bean Katmanı:** Frontend ile Backend arasındaki iletişimi ve oturum yönetimini sağlayan katman (`CustomerSessionBean`, `AdminDashboardBean`).
4.  **Güvenlik Katmanı:** Admin paneline erişimi denetleyen `AdminFilter` ve `LoginBean` mekanizması.

---

## 🎨 Öne Çıkan Özellikler

### 📱 Müşteri Deneyimi (`index.xhtml`)
* **Dijital Menü:** Kategorize edilmiş ürünler, dinamik fiyatlar ve açıklamalar.
* **Sepet Yönetimi:** Ürün ekleme/çıkarma ve gerçek zamanlı toplam tutar hesaplama.
* **Canlı Sipariş Takibi:** Siparişin durumunu (Alındı, Hazırlanıyor, Hazır vb.) gösteren animasyonlu bildirim sistemi.
* **Hızlı Servis:** Tek tıkla garson çağırma ve masaya özel servis talebi iletme.

### 🛠️ Yönetim Paneli (`admin.xhtml`)
* **Canlı Sipariş Ekranı:** Tüm masalardan gelen siparişleri anlık izleme ve durum güncelleme.
* **Garson İstekleri:** Masalardan gelen acil çağrıları takip etme ve yanıtlama.
* **Menü Editörü:** Ürün ekleme, fiyat güncelleme ve stok durumunu (satışta/tükendi) yönetme.
* **Masa Yönetimi:** İşletmedeki masaları tanımlama ve QR sistemini yapılandırma.

---

## 🌐 Uzaktan Erişim ve QR Entegrasyonu

Proje, yerel geliştirme ortamını dış dünyaya açmak ve QR kod testlerini kolaylaştırmak için aşağıdaki yöntemlerle uyumludur:

1.  **ngrok Entegrasyonu:** `ngrok http 8080` komutu ile yerel sunucunuzu global bir URL'ye taşıyabilirsiniz.
2.  **QR Kod Oluşturma:** ngrok üzerinden alınan URL (`https://xyz.ngrok-free.app/projemm/index.xhtml?table=1`) herhangi bir QR oluşturucuya verilerek masaya özel dijital menü erişimi sağlanabilir.

---

## ⚙️ Kurulum ve Çalıştırma

1.  **Veritabanı:** PostgreSQL üzerinde bir veritabanı oluşturun (Örn: `CafeDB`).
2.  **Konfigürasyon:** `src/main/resources/META-INF/persistence.xml` içindeki veritabanı bağlantı bilgilerini (URL, User, Password) güncelleyin.
3.  **Derleme:** Maven kullanarak projeyi paketleyin: 
    ```bash
    mvn clean package
    ```
4.  **Dağıtım:** Oluşan `.war` dosyasını GlassFish 7+ veya Payara Server gibi bir Jakarta EE 10 uyumlu sunucuya deploy edin.
5.  **Veri İlklendirme:** Uygulama ilk kez çalıştığında `DataSeeder` sınıfı temel kategorileri ve örnek menü ürünlerini otomatik olarak oluşturacaktır.

---

> *© 2026 tierra A LA MESA - Tüm Hakları Saklıdır.*
