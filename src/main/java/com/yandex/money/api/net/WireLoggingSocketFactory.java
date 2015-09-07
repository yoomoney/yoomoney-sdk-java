/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 NBCO Yandex.Money LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.yandex.money.api.net;

import com.yandex.money.api.utils.Numbers;

import javax.net.ssl.HandshakeCompletedListener;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.channels.SocketChannel;
import java.util.logging.Logger;

/**
 * Logging wrapper for socket factory.
 *
 * @author Roman Tsirulnikov (romanvt@yamoney.ru)
 */
final class WireLoggingSocketFactory extends SSLSocketFactory {
    private static final Logger LOG = Logger.getLogger(WireLoggingSocketFactory.class.getName());
    private final SSLSocketFactory delegate;

    WireLoggingSocketFactory(SSLSocketFactory sf0) {
        this.delegate = sf0;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return delegate.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }

    public Socket createSocket(Socket s, String host, int port,
                               boolean autoClose) throws IOException {
        LOG.info("Creating socket: " + host + ":" + port);
        return new WireLogSocket((SSLSocket) delegate.createSocket(s, host, port, autoClose));
    }

    @Override
    public Socket createSocket(String s, int i) throws IOException {
        LOG.info("Creating socket: " + s + ":" + i);
        return new WireLogSocket((SSLSocket) delegate.createSocket(s, i));
    }

    @Override
    public Socket createSocket(String s, int i, InetAddress inetAddress, int i2) throws IOException {
        LOG.info("Creating socket: " + inetAddress);
        return new WireLogSocket((SSLSocket) delegate.createSocket(s, i, inetAddress, i2));
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        LOG.info("Creating socket: " + inetAddress);
        return new WireLogSocket((SSLSocket) delegate.createSocket(inetAddress, i));
    }

    @Override
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        LOG.info("Creating socket: " + inetAddress);
        return new WireLogSocket((SSLSocket) delegate.createSocket(inetAddress, i, inetAddress2, i2));
    }

    private static class WireLogSocket extends SSLSocket {
        private final SSLSocket delegate;

        public WireLogSocket(SSLSocket s) {
            super();
            this.delegate = s;
        }

        private static void logWire(String prefix, byte[] data, int off, int len)
                throws IOException {
            StringBuilder buffer = new StringBuilder(prefix)
                    .append(" block buffer ")
                    .append(data.length)
                    .append(" offset ")
                    .append(off)
                    .append(" length ")
                    .append(len)
                    .append('\n');
            for (int i = 0; i < len; i++) {
                byte b = data[off + i];
                if (b == 13) {
                    buffer.append("[\\r]");
                } else if (b == 10) {
                    buffer.append("[\\n]\n");
                } else if (b < 32 || b > 127) {
                    buffer.append(" ").append(Numbers.byteToHex(b));
                } else {
                    buffer.append((char) b);
                }
            }
            LOG.info(buffer.toString());
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            return new LoggingOutputStream(delegate.getOutputStream());
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return new LoggingInputStream(delegate.getInputStream());
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return delegate.getSupportedCipherSuites();
        }

        @Override
        public String[] getEnabledCipherSuites() {
            return delegate.getEnabledCipherSuites();
        }

        @Override
        public void setEnabledCipherSuites(String[] strings) {
            delegate.setEnabledCipherSuites(strings);
        }

        @Override
        public String[] getSupportedProtocols() {
            return delegate.getSupportedProtocols();
        }

        @Override
        public String[] getEnabledProtocols() {
            return delegate.getEnabledProtocols();
        }

        @Override
        public void setEnabledProtocols(String[] strings) {
            delegate.setEnabledProtocols(strings);
        }

        @Override
        public SSLSession getSession() {
            return delegate.getSession();
        }

        @Override
        public void addHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
            delegate.addHandshakeCompletedListener(handshakeCompletedListener);
        }

        @Override
        public void removeHandshakeCompletedListener(HandshakeCompletedListener handshakeCompletedListener) {
            delegate.removeHandshakeCompletedListener(handshakeCompletedListener);
        }

        @Override
        public void startHandshake() throws IOException {
            delegate.startHandshake();
        }

        @Override
        public boolean getUseClientMode() {
            return delegate.getUseClientMode();
        }

        @Override
        public void setUseClientMode(boolean b) {
            delegate.setUseClientMode(b);
        }

        @Override
        public boolean getNeedClientAuth() {
            return delegate.getNeedClientAuth();
        }

        @Override
        public void setNeedClientAuth(boolean b) {
            delegate.setNeedClientAuth(b);
        }

        @Override
        public boolean getWantClientAuth() {
            return delegate.getWantClientAuth();
        }

        @Override
        public void setWantClientAuth(boolean b) {
            delegate.setWantClientAuth(b);
        }

        @Override
        public boolean getEnableSessionCreation() {
            return delegate.getEnableSessionCreation();
        }

        @Override
        public void setEnableSessionCreation(boolean b) {
            delegate.setEnableSessionCreation(b);
        }

        @Override
        public void close() throws IOException {
            delegate.close();
        }

        @Override
        public InetAddress getInetAddress() {
            return delegate.getInetAddress();
        }

        @Override
        public boolean getKeepAlive() throws SocketException {
            return delegate.getKeepAlive();
        }

        @Override
        public InetAddress getLocalAddress() {
            return delegate.getLocalAddress();
        }

        @Override
        public int getLocalPort() {
            return delegate.getLocalPort();
        }

        @Override
        public int getPort() {
            return delegate.getPort();
        }

        @Override
        public int getSoLinger() throws SocketException {
            return delegate.getSoLinger();
        }

        @Override
        public int getReceiveBufferSize() throws SocketException {
            return delegate.getReceiveBufferSize();
        }

        @Override
        public int getSendBufferSize() throws SocketException {
            return delegate.getSendBufferSize();
        }

        @Override
        public int getSoTimeout() throws SocketException {
            return delegate.getSoTimeout();
        }

        @Override
        public boolean getTcpNoDelay() throws SocketException {
            return delegate.getTcpNoDelay();
        }

        @Override
        public void setKeepAlive(boolean keepAlive) throws SocketException {
            delegate.setKeepAlive(keepAlive);
        }

        @Override
        public void setSendBufferSize(int size) throws SocketException {
            delegate.setSendBufferSize(size);
        }

        @Override
        public void setReceiveBufferSize(int size) throws SocketException {
            delegate.setReceiveBufferSize(size);
        }

        @Override
        public void setSoLinger(boolean on, int timeout) throws SocketException {
            delegate.setSoLinger(on, timeout);
        }

        @Override
        public void setSoTimeout(int timeout) throws SocketException {
            delegate.setSoTimeout(timeout);
        }

        @Override
        public void setTcpNoDelay(boolean on) throws SocketException {
            delegate.setTcpNoDelay(on);
        }

        @Override
        public String toString() {
            return "WireLogSocket={" + delegate.toString() + '}';
        }

        @Override
        public void shutdownInput() throws IOException {
            delegate.shutdownInput();
        }

        @Override
        public void shutdownOutput() throws IOException {
            delegate.shutdownOutput();
        }

        @Override
        public SocketAddress getLocalSocketAddress() {
            return delegate.getLocalSocketAddress();
        }

        @Override
        public SocketAddress getRemoteSocketAddress() {
            return delegate.getRemoteSocketAddress();
        }

        @Override
        public boolean isBound() {
            return delegate.isBound();
        }

        @Override
        public boolean isConnected() {
            return delegate.isConnected();
        }

        @Override
        public boolean isClosed() {
            return delegate.isClosed();
        }

        @Override
        public void bind(SocketAddress localAddr) throws IOException {
            delegate.bind(localAddr);
        }

        @Override
        public void connect(SocketAddress remoteAddr) throws IOException {
            delegate.connect(remoteAddr);
        }

        @Override
        public void connect(SocketAddress remoteAddr, int timeout) throws IOException {
            delegate.connect(remoteAddr, timeout);
        }

        @Override
        public boolean isInputShutdown() {
            return delegate.isInputShutdown();
        }

        @Override
        public boolean isOutputShutdown() {
            return delegate.isOutputShutdown();
        }

        @Override
        public void setReuseAddress(boolean reuse) throws SocketException {
            delegate.setReuseAddress(reuse);
        }

        @Override
        public boolean getReuseAddress() throws SocketException {
            return delegate.getReuseAddress();
        }

        @Override
        public void setOOBInline(boolean oobinline) throws SocketException {
            delegate.setOOBInline(oobinline);
        }

        @Override
        public boolean getOOBInline() throws SocketException {
            return delegate.getOOBInline();
        }

        @Override
        public void setTrafficClass(int value) throws SocketException {
            delegate.setTrafficClass(value);
        }

        @Override
        public int getTrafficClass() throws SocketException {
            return delegate.getTrafficClass();
        }

        @Override
        public void sendUrgentData(int value) throws IOException {
            delegate.sendUrgentData(value);
        }

        @Override
        public SocketChannel getChannel() {
            return delegate.getChannel();
        }

        @Override
        public void setPerformancePreferences(int connectionTime, int latency, int bandwidth) {
            delegate.setPerformancePreferences(connectionTime, latency, bandwidth);
        }

        private static class LoggingOutputStream extends FilterOutputStream {
            public LoggingOutputStream(OutputStream out) {
                super(out);
            }

            @Override
            public void write(byte[] b, int off, int len)
                    throws IOException {
                out.write(b, off, len);
                logWire("WRITE:", b, off, len);
            }

            @Override
            public void write(int b) throws IOException {
                out.write(b);
                logWire("WRITE:", new byte[]{(byte) b}, 0, 1);
            }

        }

        private static class LoggingInputStream extends FilterInputStream {
            public LoggingInputStream(InputStream out) {
                super(out);
            }

            @Override
            public int read() throws IOException {
                int b = in.read();
                if (b >= 0) {
                    logWire("READ:", new byte[]{(byte) b}, 0, 1);
                }
                return b;
            }

            @Override
            public int read(byte b[], int off, int len) throws IOException {
                int result = in.read(b, off, len);
                if (result > 0) {
                    logWire("READ:", b, off, result);
                }
                return result;
            }

        }
    }
}