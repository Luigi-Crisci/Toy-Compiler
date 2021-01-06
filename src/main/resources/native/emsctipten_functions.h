#include <emscripten.h>

EM_JS(char*, readln, (), {
  // return prompt("Insert value");ù

var jsString = prompt("Insert value");
jsString += '\n';
  // 'jsString.length' would return the length of the string as UTF-16
  // units, but Emscripten C strings operate as UTF-8.
  var lengthBytes = lengthBytesUTF8(jsString)+1;
  var stringOnWasmHeap = _malloc(lengthBytes);
  stringToUTF8(jsString, stringOnWasmHeap, lengthBytes);
  return stringOnWasmHeap;

  // return "aaaaa";
});