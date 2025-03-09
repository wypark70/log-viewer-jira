<script lang="ts">
  import { onMount } from 'svelte';
  import type { File } from './types'; // Import the File interface
  import { getIconForFile, getIconForFolder, getIconForOpenFolder } from 'vscode-icons-js';

  export let path: string = '';
  let files: File[] = [];

  async function fetchFiles(path: string) {
    const response = await fetch(`/rest/log-viewer/1.0/files?path=${path}`);
    files = await response.json();
  }

  function handleFileClick(file: File) {
    if (file.directory) {
      path = `${path === '/' ? '' : path}/${file.name}`;
      fetchFiles(path);
    } else {
      // Handle file click if needed
    }
  }

  function formatSize(size: number): string {
    if (size >= 1024 * 1024) {
      return (size / (1024 * 1024)).toFixed(2) + ' MB';
    } else {
      return (size / 1024).toFixed(2) + ' KB';
    }
  }

  function getFileIcon(file: File): string {
    if (file.directory) {
      return getIconForFolder(file.name);
    }
    return getIconForFile(file.name) || '';
  }

  onMount(() => {
    fetchFiles(path);
  });
</script>

<div class="file-table">
  <h3>{path}</h3>
  <table>
    <thead>
      <tr>
        <th>Name</th>
        <th>Type</th>
        <th>Size</th>
        <th>Last Modified</th>
      </tr>
    </thead>
    <tbody>
      {#each files as file}
        <tr>
          <td>
            <button on:click={() => handleFileClick(file)}>
              {getFileIcon(file)} {file.name}
            </button>
          </td>
          <td>{file.directory ? 'Directory' : 'File'}</td>
          <td>{formatSize(file.size)}</td>
          <td>{new Date(file.lastModified).toLocaleString()}</td>
        </tr>
      {/each}
    </tbody>
  </table>
</div>

<style>
  .file-table {
    border: 1px solid #e1e4e8;
    border-radius: 6px;
    margin: 16px 0;
  }
  h3 {
    background-color: #f6f8fa;
    border-bottom: 1px solid #e1e4e8;
    padding: 8px 16px;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji";
    font-size: 14px;
    color: #24292e;
    margin: 0;
  }
  table {
    width: 100%;
    border-collapse: collapse;
  }
  th, td {
    padding: 8px 16px;
    border: 1px solid #e1e4e8;
    text-align: left;
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji";
    font-size: 14px;
    color: #24292e;
  }
  th {
    background-color: #f6f8fa;
  }
  button {
    background: none;
    border: none;
    color: #0366d6;
    text-decoration: underline;
    cursor: pointer;
    font-family: inherit;
    font-size: inherit;
  }
</style>
