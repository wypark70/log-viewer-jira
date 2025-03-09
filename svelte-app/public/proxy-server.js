const http = require('http');
const httpProxy = require('http-proxy');

const proxy = httpProxy.createProxyServer({});

const server = http.createServer((req, res) => {
    req.headers['authorization'] = 'Basic ' + Buffer.from('admin:admin').toString('base64');
    proxy.web(req, res, { target: 'http://localhost:2990/jira' });
});

server.listen(3000, () => {
    console.log('Proxy server is running on http://localhost:3000');
});
