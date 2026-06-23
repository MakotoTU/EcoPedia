package com.makoto.ecopedia

import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage

class BarcodeAnalyzer(
    private val onBarcodeDetected: (String) -> Unit
) : ImageAnalysis.Analyzer {

    private val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
        .build()

    private val scanner = BarcodeScanning.getClient(options)
    private var isScanning = true
    
    private var lastScannedBarcode: String? = null
    private var lastScanTime: Long = 0

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        if (!isScanning) {
            imageProxy.close()
            return
        }

        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    for (barcode in barcodes) {
                        val rawValue = barcode.rawValue
                        if (rawValue != null) {
                            val currentTime = System.currentTimeMillis()
                            
                            // Cooldown 1.5 detik untuk barcode yang sama agar tidak popup terus-menerus
                            if (rawValue == lastScannedBarcode && (currentTime - lastScanTime) < 1500) {
                                continue
                            }

                            // Pause scanning to prevent multiple callbacks for different barcodes
                            isScanning = false
                            lastScannedBarcode = rawValue
                            lastScanTime = currentTime
                            
                            onBarcodeDetected(rawValue)
                            break
                        }
                    }
                }
                .addOnFailureListener {
                    // Handle any errors
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    fun resumeScanning() {
        isScanning = true
        // Reset waktu agar cooldown 3 detik dimulai saat user menutup popup
        lastScanTime = System.currentTimeMillis()
    }
}
