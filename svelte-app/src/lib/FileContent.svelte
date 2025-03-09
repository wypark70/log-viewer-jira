<script lang="ts">
  import { onMount } from 'svelte';

  export let path: string = '';
  export let lines: number = 100;
  let fileContent: string = '';

  async function fetchFileContent() {
    const response = await fetch(`/rest/log-viewer/1.0/files/read?path=${path}&lines=${lines}`);
    fileContent = await response.text();
  }

  onMount(() => {
    fetchFileContent();
  });
</script>

<div class="file-content">
  <div class="file-header">
    <span class="file-path">{path}</span>
  </div>
  <pre>{fileContent}</pre>
</div>

<style>
  .file-content {
    border: 1px solid #e1e4e8;
    border-radius: 6px;
    margin: 16px 0;
  }
  .file-header {
    background-color: #f6f8fa;
    border-bottom: 1px solid #e1e4e8;
    padding: 8px 16px;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji";
    font-size: 14px;
    color: #24292e;
  }
  .file-path {
    font-weight: 600;
  }
  pre {
    background-color: #ffffff;
    padding: 16px;
    border-radius: 0 0 6px 6px;
    margin: 0;
    font-family: SFMono-Regular, Consolas, "Liberation Mono", Menlo, Courier, monospace;
    font-size: 12px;
    line-height: 1.5;
    overflow-x: auto;
    white-space: pre-wrap;
    word-wrap: break-word;
  }
</style>
