import { defineConfig } from 'vite';
import { svelte } from '@sveltejs/vite-plugin-svelte';

export default defineConfig({
  plugins: [svelte()],
  server: {
    proxy: {
      '/rest': {
        target: 'http://localhost:2990/jira',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/rest/, '/rest'),
        auth: 'admin:admin'
      }
    }
  },
  build: {
    rollupOptions: {
      output: {
        entryFileNames: '[name].js',
        chunkFileNames: '[name].js',
        assetFileNames: '[name].[ext]'
      }
    }
  }
});
