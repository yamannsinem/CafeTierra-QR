function closeCart(data) {
    if (data.status === 'success') {
        var offcanvasElement = document.getElementById('cartOffcanvas');
        var offcanvas = bootstrap.Offcanvas.getInstance(offcanvasElement);
        if(offcanvas) { offcanvas.hide(); }
        window.scrollTo({top: 0, behavior: 'smooth'});
    }
}
// Siparis durumu otomatik yenileme (her 15 saniyede bir)
(function startOrderPolling() {
    var pollInterval = setInterval(function() {
        var banner = document.getElementById('orderStatusBanner');
        if (banner) {
            var delivered = banner.querySelector('.status-delivered');
            if (delivered) { clearInterval(pollInterval); return; }
            var btn = document.querySelector('[id*="refreshBtn"]');
            if (btn) { btn.click(); }
        }
    }, 15000);
})();
