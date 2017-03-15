set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 6]);

y=[7.23E-04	0.003918837	0.004946323;
0.002944432	0.044391757	0.056787164;
0.002542475	0.00549872	0.016983534;
2.58E-04	0.00689637	0.004990481];

b=bar(y);

ylabel('CWF');

axis([0.5 4.5 0.0 0.1]);

set(gca, 'xtick', 1:4, 'XTickLabel', {'Flickr','DBLP','Tencent','DBpedia'});
leg=legend('Global','Local','Dec', 1);
set(leg,'edgecolor','white');

set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);
applyhatch(gcf,'\x/') ;
