set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 6]);

y=[0.03645	0.0488	0.0396	0.1239	0.2149	0.133927759;
   0.05279	0.1379	0.2244	0.4465	0.51	0.678014713];

b=bar(y);

ylabel('CMF');

axis([0.5 2.5 0.0 1.0]);

set(gca, 'xtick', 1:2, 'XTickLabel', {'Flickr','DBLP'});
% leg=legend('Codicil(2.5k)','Codicil(5.0k)','Codicil(7.5k)','Codicil(10k)','LAC');
% set(leg,'edgecolor','white');

set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'FontSize', 12);
leg1 = legend(b(:,1:5)','Cod1K','Cod5K','Cod10K', 'Cod50K', 'Cod100K', 2);

set(leg1,'edgecolor','white');

set(gca, 'LineWidth', 1.5);
ah=axes('position',get(gca,'position'),'visible','off');
set(gca, 'FontSize', 12);
leg2 = legend(ah,b(:,6:6)','ACQ', 1);
set(leg2,'edgecolor','white');


set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);
applyhatch(gcf,'/\-+.x') ;