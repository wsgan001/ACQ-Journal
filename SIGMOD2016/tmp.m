set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 6]);

y=[0.1 0.2 0.3 0.4 0.5;
   0.15 0.25 0.35 0.45 0.55];

b=bar(y);

ylabel('CMF');

axis([0.5 2.5 0.0 1.0]);

set(gca, 'xtick', 1:2, 'XTickLabel', {'Flickr','DBLP'});
% leg=legend('Codicil(2.5k)','Codicil(5.0k)','Codicil(7.5k)','Codicil(10k)','LAC');
% set(leg,'edgecolor','white');

set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'FontSize', 12);
leg1 = legend(b(:,1:3)','Cod2.5k','Cod5.0k','Cod10k', 2);

set(leg1,'edgecolor','white');

set(gca, 'LineWidth', 1.5);
ah=axes('position',get(gca,'position'),'visible','off');
set(gca, 'FontSize', 12);
leg2 = legend(ah,b(:,4:5)','LAC1','ABC', 1);
set(leg2,'edgecolor','white');



set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);
applyhatch(gcf,'/+\-x') ;
