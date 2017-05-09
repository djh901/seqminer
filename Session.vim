let SessionLoad = 1
let s:so_save = &so | let s:siso_save = &siso | set so=0 siso=0
let v:this_session=expand("<sfile>:p")
silent only
cd ~/seqminer
if expand('%') == '' && !&modified && line('$') <= 1 && getline(1) == ''
  let s:wipebuf = bufnr('%')
endif
set shortmess=aoO
badd +0 src/main/java/weka/filters/unsupervised/attribute/AminoAcidCounts.java
badd +0 src/main/java/weka/filters/unsupervised/attribute/AminoAcidFilter.java
badd +0 src/main/java/weka/filters/unsupervised/attribute/DipeptideCounts.java
badd +0 src/main/java/weka/filters/unsupervised/attribute/KideraFactors.java
badd +0 src/main/java/weka/filters/unsupervised/attribute/SlidingWindow.java
badd +0 src/test/java/weka/filters/unsupervised/attribute/AminoAcidCountTest.java
badd +0 src/test/java/weka/filters/unsupervised/attribute/AminoAcidFilterTest.java
badd +0 src/test/java/weka/filters/unsupervised/attribute/KideraFactorTest.java
argglobal
silent! argdel *
argadd src/main/java/weka/filters/unsupervised/attribute/AminoAcidCounts.java
argadd src/main/java/weka/filters/unsupervised/attribute/AminoAcidFilter.java
argadd src/main/java/weka/filters/unsupervised/attribute/DipeptideCounts.java
argadd src/main/java/weka/filters/unsupervised/attribute/KideraFactors.java
argadd src/main/java/weka/filters/unsupervised/attribute/SlidingWindow.java
argadd src/test/java/weka/filters/unsupervised/attribute/AminoAcidCountTest.java
argadd src/test/java/weka/filters/unsupervised/attribute/AminoAcidFilterTest.java
argadd src/test/java/weka/filters/unsupervised/attribute/KideraFactorTest.java
edit src/main/java/weka/filters/unsupervised/attribute/AminoAcidCounts.java
set splitbelow splitright
set nosplitbelow
set nosplitright
wincmd t
set winheight=1 winwidth=1
argglobal
setlocal fdm=manual
setlocal fde=0
setlocal fmr={{{,}}}
setlocal fdi=#
setlocal fdl=0
setlocal fml=1
setlocal fdn=20
setlocal fen
silent! normal! zE
let s:l = 1 - ((0 * winheight(0) + 36) / 72)
if s:l < 1 | let s:l = 1 | endif
exe s:l
normal! zt
1
normal! 0
tabnext 1
if exists('s:wipebuf') && getbufvar(s:wipebuf, '&buftype') isnot# 'terminal'
  silent exe 'bwipe ' . s:wipebuf
endif
unlet! s:wipebuf
set winheight=1 winwidth=20 shortmess=filnxtToO
let s:sx = expand("<sfile>:p:r")."x.vim"
if file_readable(s:sx)
  exe "source " . fnameescape(s:sx)
endif
let &so = s:so_save | let &siso = s:siso_save
doautoall SessionLoadPost
unlet SessionLoad
" vim: set ft=vim :
