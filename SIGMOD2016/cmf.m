set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 6]);

y=[0.012585601	0.078621953	0.133927759;
0.040356552	0.564740645	0.678014713;
0.01892354	0.102500556	0.292395349;
0.005539661	0.159790919	0.198729764];

b=bar(y);

ylabel('CMF');

axis([0.5 4.5 0.0 1.0]);

set(gca, 'xtick', 1:4, 'XTickLabel', {'Flickr','DBLP','Tencent','DBpedia'});
leg=legend('Global','Local','ACQ', 1);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);
applyhatch(gcf,'/\x') ;
