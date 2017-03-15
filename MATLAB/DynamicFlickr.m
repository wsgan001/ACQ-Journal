set(gcf,'color',[1, 1, 1]);
set(gcf,'unit','centimeters','position',[10 5 9 7]);

x=[1, 2, 3, 4, 5];
y1 = [13322.62 13309.199   13289.235	13398.69	 13321.05];
y2 = [136.4687	116.123 102.912	93.407	99.557];

p1= semilogy(x, y1, '-k^');
hold on;
p2 = semilogy(x, y2, '-ko');
hold on;

xlabel('interval of the evolving graph (days)');
ylabel('time (ms)');

axis([0.5 5.5 90.0 20000]);

set(gca, 'xtick', 1:5, 'XTickLabel', {'20','40','60','80','100'});
leg=legend('rebuild','update', 2);
set(leg,'edgecolor','white');
set(gca,'ytick',[10^2,10^3,10^4,10^5]);
set(gca, 'FontSize', 12);
set(get(gca, 'XLabel'), 'FontSize',12);
set(get(gca, 'YLabel'), 'FontSize',12);
set(findall(gcf,'type','line'),'linewidth',1.5);
set(gca, 'LineWidth', 1.5);