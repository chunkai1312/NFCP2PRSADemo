# NFCP2PRSADemo
103-1 NTUST RFID資訊安全 Programming Homework #4

## Requirements
以第三次作業為基礎，結合對稱與非對稱式加密演算法實作加密與解密功能：

> 程式安裝時，內置一對 RSA 公鑰 (public key) 與私鑰 (private key)，使用者則產生一把對稱式金鑰 (選用 DES ／3DES／AES 實作)。

> 為求簡化發佈公鑰的流程，該程式產生相同的公鑰與私鑰即可。

### Beaming Message
* 將文字訊息經過對稱式金鑰加密後建立 TNF_WELL_KNOWN with RTD_TEXT 型態的 Record。
* 將使用者的對稱式金鑰使用 RSA 公鑰加密後建立 TNF_WELL_KNOWN with RTD_TEXT 型態的 Record。
* 將加密訊息與加密金鑰透過 Android Beam 功能發送 NDEF Message。

### Receiving Message 
* 透過  Android Beam 功能接收 NDEF Message。
* 加密的金鑰使用 RSA 私鑰解出發送端的對稱式金鑰。
* 使用解密後的對稱式金鑰解開密文 (ciphertext) 後產生明文 (plaintext)。