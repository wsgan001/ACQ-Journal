set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 6]);

y=[0.006076043	0.058441736	0.057996121;
0.02724782	0.449528565	0.556974079;
0.010468778	0.079753294	0.101233887;
0.0024267	0.103567254	0.079101432];

b=bar(y);

ylabel('CQJ');

axis([0.5 4.5 0.0 1.0]);

set(gca, 'xtick', 1:4, 'XTickLabel', {'Flickr','DBLP','Tencent','DBpedia'});
leg=legend('Global','Local','Dec', 1);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);
applyhatch(gcf,'\x/') ;
